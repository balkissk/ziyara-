package com.ziyara.dto;

public record AvisCreateRequest(
        Long voyageId,
        int note,
        String commentaire
) {}
