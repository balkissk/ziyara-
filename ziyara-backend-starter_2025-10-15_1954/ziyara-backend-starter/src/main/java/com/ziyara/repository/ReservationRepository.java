package com.ziyara.repository;

public interface ReservationRepository extends org.springframework.data.jpa.repository.JpaRepository<com.ziyara.model.Reservation, Long> { java.util.List<com.ziyara.model.Reservation> findByUtilisateurId(Long userId);}
