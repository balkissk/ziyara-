package com.ziyara.repository;

public interface RecommandationRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ziyara.model.Recommandation, Long> { java.util.List<com.ziyara.model.Recommandation> findByUtilisateurId(Long userId);}
