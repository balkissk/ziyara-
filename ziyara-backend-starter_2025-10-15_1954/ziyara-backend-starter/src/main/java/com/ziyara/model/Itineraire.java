package com.ziyara.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Itineraire {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private int duree;

    @Column(length = 4000)
    private String details;

    @ManyToOne
    private Voyage voyage;
}
