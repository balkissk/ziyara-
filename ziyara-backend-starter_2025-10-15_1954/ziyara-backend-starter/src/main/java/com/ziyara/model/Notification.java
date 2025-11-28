package com.ziyara.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type; // RESERVATION_CREATED, RESERVATION_STATUS_CHANGED, ...

    @Column(length = 1000)
    private String message;

    private LocalDateTime dateEnvoi;

    private boolean lu; // read/unread

    private Long reservationId; // to deep-link the user to the reservation in the UI

    @ManyToOne(optional = false)
    private Utilisateur utilisateur;

    @PrePersist
    void onCreate() {
        if (dateEnvoi == null) dateEnvoi = LocalDateTime.now();
    }

    public enum Type { RESERVATION_CREATED, RESERVATION_STATUS_CHANGED }
}
