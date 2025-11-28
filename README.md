<div align="center">

<img src="Frontend/src/assets/logo.png" width="230" alt="Ziyara Logo"/>

# 🧭 Ziyara Travel – Web Platform for Travel Discovery & Online Reservation  

A full-stack tourism platform built with **Angular**, **Spring Boot**, **Flask (Python)** and **Docker**.  
Developed as part of an engineering internship at **BeeCoders**.  
</div>

<p align="center">
  <img src="https://img.shields.io/badge/Angular-17-red"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3-brightgreen"/>
  <img src="https://img.shields.io/badge/Python-Flask-blue"/>
  <img src="https://img.shields.io/badge/AI%20Recommender-TF--IDF%20%2B%20MMR-orange"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-blue"/>
  <img src="https://img.shields.io/badge/Docker-Containerized-2496ED"/>
</p>

---

# 📝 Overview

**Ziyara Travel** est une plateforme web moderne dédiée au tourisme tunisien.  
Elle permet aux utilisateurs de :

- Consulter les destinations touristiques 📍  
- Réserver des voyages en ligne 💳  
- Gérer leurs réservations 🧾  
- Laisser des avis ✍️  
- Recevoir des notifications 🔔  
- Voir des recommandations intelligentes basées sur leurs préférences 🤖

Le projet reprend toute la démarche décrite dans ton **rapport de stage d’ingénierie** :contentReference[oaicite:1]{index=1} :  
analyse, conception, architecture, sprints, réalisation et tests.

---

# ✨ Key Features

<div align="center">
  <table>
    <tr>
      <td align="center" width="33%">
        <b>👤 Authentication & Registration</b><br/>
        Secure login, JWT, account creation
      </td>
      <td align="center" width="33%">
        <b>🧳 Travel Management</b><br/>
        Explore destinations, details, images, prices
      </td>
      <td align="center" width="33%">
        <b>📝 Booking System</b><br/>
        Reservation, modification, cancellation
      </td>
    </tr>
    <tr>
      <td align="center">
        <b>⭐ Reviews & Ratings</b><br/>
        Add, edit & delete comments + star rating
      </td>
      <td align="center">
        <b>🔔 Notifications</b><br/>
        Real-time updates for reservations
      </td>
      <td align="center">
        <b>🤖 Smart Recommender</b><br/>
        AI recommendations (TF-IDF + MMR)
      </td>
    </tr>
  </table>
</div>

---

# 🤖 AI Recommender System  
(From your report – Chapter 4, page 31 :contentReference[oaicite:2]{index=2})

Ziyara intègre un moteur intelligent basé sur :

### ✔ TF-IDF  
Analyse la similarité textuelle entre les descriptions de voyages.

### ✔ MMR (Maximal Marginal Relevance)  
Améliore la diversité des recommandations.

### Pipeline :
1. Angular → requête à Flask (`/similar/{id}`)  
2. Flask → charge les voyages via SQLAlchemy  
3. Calcul similarité cosinus  
4. Réordonnancement MMR  
5. Renvoi de **5 voyages similaires**  
6. Angular → affiche les suggestions

---

# 🏗 Architecture

## 🔹 Architecture Logique  
(basée sur chapitre 2, page 14–16 :contentReference[oaicite:3]{index=3})

### Couches :

- **Présentation** → Angular  
- **Métier** → Spring Boot  
- **IA** → Flask (Python)  
- **Persistance** → MySQL  
- **Containers** → Docker (multi-services)

---

## 🔹 Architecture Physique  
(du rapport – page 18–19 :contentReference[oaicite:4]{index=4})

<div align="center">
<img src="docs/architecture-physique.png" width="800">
</div>

**Conteneurs Docker :**
- Angular Container  
- Spring Boot Container  
- Flask AI Container  
- MySQL Database  
- Internal Docker network `ziyara-net`

---

# 📚 SCRUM Methodology  
(from page 6–7 of the report :contentReference[oaicite:5]{index=5})

### Rôles :
- Product Owner → Ahmed Neffati  
- Scrum Master → Ahmed Neffati  
- Dev Team → Balkis Sekri  

### Organisation :
- Sprints de 2 semaines  
- Backlogs → Sprint 1 & Sprint 2  
- Daily Meetings  
- Reviews & Retrospectives  

---

# 🧩 Sprint Breakdown

## 🟦 Sprint 1 – Authentication  
✔ Inscription  
✔ Connexion  
✔ JWT  
✔ Validation formulaire  
✔ Séquence complète (front → back → DB)

## 🟧 Sprint 2 – Main Features  
✔ Affichage voyages  
✔ Réservation  
✔ Notifications  
✔ Avis  
✔ Profil utilisateur  
✔ Recommandation IA  

---

# 🖼 Interface Screenshots  
(Extraits de ton rapport pages 33–35 :contentReference[oaicite:6]{index=6})

| Page d’accueil | Réservation | Avis |
|----------------|------------|------|
| <img src="docs/home.png" width="250"/> | <img src="docs/reservation.png" width="250"/> | <img src="docs/reviews.png" width="250"/> |

*(Ajoute les images du projet dans /docs et change les chemins.)*

---

# 🛠 Technologies

### Frontend  
- Angular 17  
- TypeScript  
- HTML / CSS  
- Bootstrap  

### Backend  
- Spring Boot 3  
- Spring Security (JWT)  
- Spring Data JPA  
- Maven  

### IA  
- Flask (Python)  
- TF-IDF  
- MMR  
- SQLAlchemy  

### Database  
- MySQL  

### DevOps  
- Docker  
- Postman  
- GitHub  

---

# 🗂 Directory Structure


---

# 🚀 Getting Started

## 🔧 Backend
```bash
cd Backend
mvn spring-boot:run


