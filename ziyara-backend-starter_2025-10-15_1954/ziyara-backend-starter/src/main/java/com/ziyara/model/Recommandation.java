package com.ziyara.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recommandation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // BASEE_SUR_REGION, HISTORIQUE, POPULARITE
    @Column(length = 1000)
    private String description;
    private Double score;

    @ManyToOne
    private Utilisateur utilisateur;
}
