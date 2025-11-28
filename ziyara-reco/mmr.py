import os
from flask import Flask, jsonify, request
from sqlalchemy import create_engine, text
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from dotenv import load_dotenv
from mmr import mmr_rerank
from flask_cors import CORS
import numpy as np


# ---------- Config ----------
load_dotenv()

DB_USER = os.getenv("DB_USER", "root")
DB_PASS = os.getenv("DB_PASS", "root")
DB_HOST = os.getenv("DB_HOST", "localhost")
DB_PORT = int(os.getenv("DB_PORT", "3306"))
DB_NAME = os.getenv("DB_NAME", "ziyara_db")

ENGINE = create_engine(
    f"mysql+pymysql://{DB_USER}:{DB_PASS}@{DB_HOST}:{DB_PORT}/{DB_NAME}"
    f"?charset=utf8mb4",
    pool_pre_ping=True,
    pool_recycle=3600,
)

# Adjust table/columns to your schema (matches your Java entity Voyage)
# id | titre | region | description | image_url | prix | date_depart | date_retour | places_disponibles
SELECT_SQL = text("""
    SELECT
      id,
      titre,
      region,
      description,
      image_url   AS imageUrl,
      prix,
      date_depart AS dateDepart,
      date_retour AS dateRetour,
      places_disponibles AS placesDisponibles
    FROM voyage
    WHERE description IS NOT NULL
""")

def _build_text(v: dict) -> str:
    """Text used for similarity. Tweak freely."""
    return f"{v.get('titre','')} {v.get('region','')} {v.get('description','')}"

# ---------- App ----------
app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})  #
try:
    app.json.ensure_ascii = False  # Flask 2.2+ / 3.x
except Exception:
    # Fallback for older Flask
    app.config['JSON_AS_ASCII'] = False

# In-memory index
_catalog = []          # list of voyages (dicts)
_id_to_idx = {}        # { voyage_id: row_index }
_vectorizer = None     # TF-IDF vectorizer
_matrix = None         # sparse matrix

def reindex():
    """Load voyages from DB and rebuild TF-IDF index."""
    global _catalog, _id_to_idx, _vectorizer, _matrix
    with ENGINE.connect() as conn:
        rows = [dict(r._mapping) for r in conn.execute(SELECT_SQL)]

    _catalog = rows
    _id_to_idx = {v["id"]: i for i, v in enumerate(_catalog)}

    texts = [_build_text(v) for v in _catalog]
    # French stopwords; bigrams help short texts
    FRENCH_STOPWORDS = [
        "le", "la", "les", "de", "du", "des", "un", "une", "et", "en", "dans", "pour",
        "par", "sur", "avec", "ce", "cet", "cette", "ces", "a", "au", "aux", "plus",
        "ou", "où", "se", "sa", "son", "ses", "que", "qui", "quoi", "dont", "ne",
        "pas", "plus", "sans", "vers", "comme", "entre", "chez", "être", "avoir"
    ]
    _vectorizer = TfidfVectorizer(stop_words=FRENCH_STOPWORDS, ngram_range=(1, 2), max_features=20000)

    _matrix = _vectorizer.fit_transform(texts)

    return {"indexed": len(_catalog)}

def _ensure_index_for(voyage_id: int) -> bool:
    """Build index if missing; ensure voyage exists."""
    if _matrix is None or voyage_id not in _id_to_idx:
        reindex()
    return voyage_id in _id_to_idx

@app.get("/health")
def health():
    return {
        "status": "ok",
        "indexed": len(_catalog),
        "db": f"{DB_HOST}:{DB_PORT}/{DB_NAME}"
    }

@app.post("/reindex")
def trigger_reindex():
    return reindex(), 200

@app.get("/similar/<int:voyage_id>")
def similar(voyage_id: int):
    if not _ensure_index_for(voyage_id):
        return jsonify({"error": f"voyage {voyage_id} not found"}), 404

    # --- Defaults ---
    k = int(request.args.get("k", 5))
    min_sim = float(request.args.get("min_sim", 0.0))
    mmr_lambda = float(request.args.get("mmr_lambda", 0.7))
    pool_n = int(request.args.get("pool_n", max(50, k * 5)))

    idx = _id_to_idx[voyage_id]
    query_vec = _matrix[idx]
    base_voyage = _catalog[idx]

    sims = cosine_similarity(query_vec, _matrix).ravel()
    order = sims.argsort()[::-1]

    candidate_indices = []
    for j in order:
        if j == idx:
            continue
        if sims[j] < min_sim:
            continue
        candidate_indices.append(j)
        if len(candidate_indices) >= pool_n:
            break

    if not candidate_indices:
        return jsonify([]), 200

    from mmr import mmr_rerank
    selected = mmr_rerank(
        query_vec=query_vec,
        doc_matrix=_matrix,
        candidate_indices=candidate_indices,
        top_k=k,
        lambda_=mmr_lambda
    )

    def build_reason(base, other):
        """Human-readable reason why two voyages are similar."""
        reasons = []
        if base["region"].lower() == other["region"].lower():
            reasons.append(f"même région ({base['region']})")
        # keyword overlap
        base_words = set(base["description"].lower().split())
        other_words = set(other["description"].lower().split())
        common = base_words & other_words
        if common:
            # pick top few shared meaningful words
            stop = {"le", "la", "les", "de", "et", "un", "une", "pour", "dans", "en"}
            shared = [w for w in common if len(w) > 4 and w not in stop]
            if shared:
                reasons.append(f"mots communs: {', '.join(shared[:3])}")
        return "; ".join(reasons) if reasons else "thème similaire"

    recs = []
    for j in selected:
        rec = _catalog[j].copy()
        rec["reason"] = build_reason(base_voyage, rec)
        recs.append(rec)

    return jsonify({
        "voyage_id": voyage_id,
        "count": len(recs),
        "mmr_lambda": mmr_lambda,
        "min_sim": min_sim,
        "recs": recs
    }), 200



if __name__ == "__main__":
    try:
        reindex()
    except Exception as e:
        print("WARN: initial reindex failed ->", e)
    app.run(host="0.0.0.0", port=5000, debug=True)
