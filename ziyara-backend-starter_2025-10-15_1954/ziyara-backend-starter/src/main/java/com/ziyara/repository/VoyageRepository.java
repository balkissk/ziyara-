package com.ziyara.repository;

public interface VoyageRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ziyara.model.Voyage, Long> { java.util.List<com.ziyara.model.Voyage> findByRegionIgnoreCase(String region);}
