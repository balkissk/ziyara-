package com.ziyara.dto;

import java.time.LocalDate;

public record AvisDTO(
        Long id,
        int note,
        String commentaire,
        LocalDate dateAvis,
        String userNom,
        String userPrenom,
        String userEmail
) {}
