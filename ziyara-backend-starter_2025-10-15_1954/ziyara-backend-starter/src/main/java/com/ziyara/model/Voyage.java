package com.ziyara.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Voyage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String region;
    private Double prix;
    private LocalDate dateDepart;
    private LocalDate dateRetour;

    @Column(length = 1200)
    private String description;

    private String imageUrl;
    private int placesDisponibles = 0;
    private double noteMoyenne = 0.0;
}
