package com.ziyara.service;

import com.ziyara.model.Notification;
import com.ziyara.model.Reservation;
import com.ziyara.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    @Transactional
    public void onReservationCreated(Reservation r) {
        Notification n = Notification.builder()
                .type(Notification.Type.valueOf("RESERVATION_CREATED"))
                .message("Votre réservation #" + r.getId() +
                        " pour \"" + r.getVoyage().getTitre() + "\" a été créée.")
                .utilisateur(r.getUtilisateur())
                .build();
        repo.save(n);
    }

    @Transactional
    public void onReservationStatusChanged(Reservation r, Reservation.Statut oldS, Reservation.Statut newS) {
        Notification n = Notification.builder()
                .type(Notification.Type.valueOf("RESERVATION_STATUS_CHANGED"))
                .message("Le statut de votre réservation #" + r.getId() +
                        " est passé de " + oldS + " à " + newS + ".")
                .utilisateur(r.getUtilisateur())
                .build();
        repo.save(n);
    }
}
