package com.ziyara.service;

import com.ziyara.model.Reservation;
import com.ziyara.model.Voyage;
import com.ziyara.repository.ReservationRepository;
import com.ziyara.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminReservationService {

    private final ReservationRepository reservationRepo;
    private final VoyageRepository voyageRepo;
    private final NotificationService notificationService;

    /** Admin: confirm a reservation (EN_ATTENTE -> CONFIRMEE) */
    @Transactional
    public Reservation confirm(Long reservationId) {
        Reservation r = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable: " + reservationId));

        if (r.getStatut() == Reservation.Statut.ANNULEE) {
            throw new IllegalStateException("Impossible de confirmer une réservation déjà annulée.");
        }
        if (r.getStatut() == Reservation.Statut.CONFIRMEE) {
            return r;
        }

        Reservation.Statut old = r.getStatut();
        r.setStatut(Reservation.Statut.CONFIRMEE);
        Reservation saved = reservationRepo.save(r);
        notificationService.onReservationStatusChanged(saved, old, Reservation.Statut.CONFIRMEE);
        return saved;
    }

    /** Admin: cancel a reservation
     * Rules:
     * - If already ANNULEE: idempotent return
     * - If EN_ATTENTE or CONFIRMEE -> ANNULEE and restore seats
     */
    @Transactional
    public Reservation cancel(Long reservationId) {
        Reservation r = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable: " + reservationId));

        if (r.getStatut() == Reservation.Statut.ANNULEE) {
            return r;
        }

        // restore seats
        Voyage v = r.getVoyage();
        v.setPlacesDisponibles(v.getPlacesDisponibles() + r.getNombrePersonnes());
        voyageRepo.save(v);

        Reservation.Statut old = r.getStatut();
        r.setStatut(Reservation.Statut.ANNULEE);
        Reservation saved = reservationRepo.save(r);
        notificationService.onReservationStatusChanged(saved, old, Reservation.Statut.ANNULEE);
        return saved;
    }
}
