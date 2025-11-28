package com.ziyara.dto;

import java.time.LocalDate;

public record VoyageBriefDTO(
        Long id,
        String titre,
        String region,
        double prix,
        LocalDate dateDepart,
        LocalDate dateRetour,
        String imageUrl
) {}