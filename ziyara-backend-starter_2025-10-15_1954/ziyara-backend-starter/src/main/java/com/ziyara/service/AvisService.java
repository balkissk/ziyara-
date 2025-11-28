package com.ziyara.service;

import com.ziyara.model.Avis;
import com.ziyara.model.Utilisateur;
import com.ziyara.model.Voyage;
import com.ziyara.repository.AvisRepository;
import com.ziyara.repository.UtilisateurRepository;
import com.ziyara.repository.VoyageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvisService {

    private final AvisRepository repo;
    private final VoyageRepository voyageRepo;
    private final UtilisateurRepository userRepo;

    public AvisService(AvisRepository repo, VoyageRepository voyageRepo, UtilisateurRepository userRepo) {
        this.repo = repo;
        this.voyageRepo = voyageRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Avis createOrUpdate(String userEmail, Long voyageId, int note, String commentaire) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5.");
        }

        Utilisateur u = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + userEmail));

        Voyage v = voyageRepo.findById(voyageId)
                .orElseThrow(() -> new RuntimeException("Voyage introuvable avec l'ID : " + voyageId));

        return repo.findByUtilisateurEmailAndVoyageId(userEmail, voyageId)
                .map(existing -> {
                    // ✅ Update existing
                    existing.setNote(note);
                    existing.setCommentaire(commentaire);
                    existing.setDateAvis(java.time.LocalDate.now());
                    return repo.save(existing);
                })
                .orElseGet(() -> {
                    // ✅ Create new
                    Avis a = Avis.builder()
                            .note(note)
                            .commentaire(commentaire)
                            .dateAvis(java.time.LocalDate.now())
                            .utilisateur(u)
                            .voyage(v)
                            .build();
                    return repo.save(a);
                });
    }

    public List<Avis> byVoyage(Long voyageId) {
        return repo.findByVoyageId(voyageId);
    }

    public double averageRating(Long voyageId) {
        List<Avis> avis = repo.findByVoyageId(voyageId);
        if (avis.isEmpty()) return 0.0;
        double sum = avis.stream().mapToInt(Avis::getNote).sum();
        return Math.round((sum / avis.size()) * 10.0) / 10.0; // round to 1 decimal
    }
    @Transactional
    public void deleteOwned(String userEmail, Long avisId) {
        Avis avis = repo.findByIdAndUtilisateurEmail(avisId, userEmail)
                .orElseThrow(() -> new RuntimeException("Avis introuvable ou non autorisé"));
        repo.delete(avis);
    }
}
