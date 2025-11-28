package com.ziyara.repository;

import com.ziyara.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ziyara.model.Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

}
