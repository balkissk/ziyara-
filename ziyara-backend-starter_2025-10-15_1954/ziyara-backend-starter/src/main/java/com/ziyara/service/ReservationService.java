package com.ziyara.service;

import com.ziyara.model.Reservation;
import com.ziyara.model.Utilisateur;
import com.ziyara.model.Voyage;
import com.ziyara.repository.ReservationRepository;
import com.ziyara.repository.UtilisateurRepository;
import com.ziyara.repository.VoyageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final VoyageRepository voyageRepo;
    private final UtilisateurRepository userRepo;
    private final NotificationService notificationService;

    public ReservationService(ReservationRepository r,
                              VoyageRepository v,
                              UtilisateurRepository u,
                              NotificationService notificationService) {
        this.reservationRepo = r;
        this.voyageRepo = v;
        this.userRepo = u;
        this.notificationService = notificationService;
    }

    @Transactional
    public Reservation createForUser(String userEmail, Long voyageId, int personnes) {
        if (personnes <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être > 0");
        }

        Utilisateur u = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + userEmail));

        Voyage v = voyageRepo.findById(voyageId)
                .orElseThrow(() -> new RuntimeException("Voyage introuvable avec l'ID : " + voyageId));

        if (v.getPlacesDisponibles() < personnes) {
            throw new IllegalArgumentException("Places insuffisantes pour ce voyage");
        }

        // décrémentation
        v.setPlacesDisponibles(v.getPlacesDisponibles() - personnes);
        voyageRepo.save(v);

        Reservation res = Reservation.builder()
                .utilisateur(u)
                .voyage(v)
                .nombrePersonnes(personnes)
                .dateReservation(LocalDate.now())
                .statut(Reservation.Statut.EN_ATTENTE)
                .build();

        Reservation saved = reservationRepo.save(res);

        // ✅ Notification: reservation created
        notificationService.onReservationCreated(saved);

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Reservation> byUserEmail(String email) {
        Utilisateur u = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + email));
        return reservationRepo.findByUtilisateurId(u.getId());
    }

    @Transactional(readOnly = true)
    public List<Reservation> allReservations() {
        return reservationRepo.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        if (!reservationRepo.existsById(id)) {
            throw new RuntimeException("Réservation introuvable avec l'ID : " + id);
        }
        reservationRepo.deleteById(id);
        // (Optionnel) vous pouvez aussi créer une notification de suppression si vous voulez.
    }

    // ✅ New: update status + notify on change
    @Transactional
    public Reservation updateStatus(Long reservationId, Reservation.Statut newStatut) {
        Reservation r = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable avec l'ID : " + reservationId));

        Reservation.Statut old = r.getStatut();
        if (old != newStatut) {
            r.setStatut(newStatut);
            Reservation saved = reservationRepo.save(r);

            // ✅ Notification: status changed
            notificationService.onReservationStatusChanged(saved, old, newStatut);

            return saved;
        }
        return r; // pas de changement -> pas de notification
    }
}
