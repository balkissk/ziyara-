package com.ziyara.dto;

import java.time.LocalDate;

public record ReservationDTO(
        Long id,
        LocalDate dateReservation,
        int nombrePersonnes,
        String statut,
        VoyageBriefDTO voyage
) {}
