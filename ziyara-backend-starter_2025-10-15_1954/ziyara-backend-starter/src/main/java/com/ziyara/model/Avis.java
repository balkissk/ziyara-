package com.ziyara.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "avis",
        uniqueConstraints = @UniqueConstraint(columnNames = {"utilisateur_id", "voyage_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Avis {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int note;

    @Column(length = 1000)
    private String commentaire;

    @Column(nullable = false)
    private LocalDate dateAvis = LocalDate.now();

    @ManyToOne(optional = false)
    private Utilisateur utilisateur;

    @ManyToOne(optional = false)
    private Voyage voyage;
}
