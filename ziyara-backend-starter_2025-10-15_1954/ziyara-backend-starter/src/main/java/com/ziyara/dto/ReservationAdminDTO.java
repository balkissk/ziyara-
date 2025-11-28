package com.ziyara.dto;

import java.time.LocalDate;

public record ReservationAdminDTO(
        Long id,
        LocalDate dateReservation,
        int nombrePersonnes,
        String statut,
        String userNom,
        String userPrenom,
        String userEmail,
        VoyageBriefDTO voyage
) {}
