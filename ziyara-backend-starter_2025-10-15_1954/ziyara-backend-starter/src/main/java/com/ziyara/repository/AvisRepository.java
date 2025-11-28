package com.ziyara.repository;

import com.ziyara.model.Avis;

import java.util.List;
import java.util.Optional;

public interface AvisRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ziyara.model.Avis, Long> {
    List<Avis> findByVoyageId(Long voyageId);
    Optional<Avis> findByUtilisateurEmailAndVoyageId(String email, Long voyageId);

    Optional<Avis> findByIdAndUtilisateurEmail(Long id, String email);
}
