package com.ziyara.service;

import com.ziyara.model.Voyage;
import com.ziyara.repository.VoyageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoyageService {

    private final VoyageRepository voyageRepository;

    public VoyageService(VoyageRepository voyageRepository) {
        this.voyageRepository = voyageRepository;
    }

    public List<Voyage> list() {
        return voyageRepository.findAll();
    }

    public Voyage get(Long id) {
        return voyageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voyage introuvable avec l'ID : " + id));
    }

    public Voyage save(Voyage v) {
        return voyageRepository.save(v);
    }

    public void delete(Long id) {
        if (!voyageRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : voyage introuvable avec l'ID " + id);
        }
        voyageRepository.deleteById(id);
    }
    public Voyage update(Long id, Voyage updated) {
        Voyage existing = voyageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voyage introuvable avec l'ID : " + id));

        if (updated.getTitre() != null) existing.setTitre(updated.getTitre());
        if (updated.getRegion() != null) existing.setRegion(updated.getRegion());
        if (updated.getPrix() != null) existing.setPrix(updated.getPrix());
        if (updated.getDateDepart() != null) existing.setDateDepart(updated.getDateDepart());
        if (updated.getDateRetour() != null) existing.setDateRetour(updated.getDateRetour());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getImageUrl() != null) existing.setImageUrl(updated.getImageUrl());

        if (updated.getPlacesDisponibles() > 0) existing.setPlacesDisponibles(updated.getPlacesDisponibles());
        if (updated.getNoteMoyenne() > 0) existing.setNoteMoyenne(updated.getNoteMoyenne());

        return voyageRepository.save(existing);
    }

}
