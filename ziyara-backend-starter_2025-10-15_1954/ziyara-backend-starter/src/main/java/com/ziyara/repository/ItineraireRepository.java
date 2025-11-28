package com.ziyara.repository;

public interface ItineraireRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ziyara.model.Itineraire, Long> { java.util.List<com.ziyara.model.Itineraire> findByVoyageId(Long voyageId);}
