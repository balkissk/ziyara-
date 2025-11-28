package com.ziyara.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paiement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montant;
    private LocalDate datePaiement = LocalDate.now();
    private String methode; // ex: CARTE, CASH
    private String statut;  // ex: SUCCES, ECHEC, EN_ATTENTE

    @OneToOne
    private Reservation reservation;
}
