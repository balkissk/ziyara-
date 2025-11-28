package com.ziyara.mappers;

import com.ziyara.dto.ReservationAdminDTO;
import com.ziyara.dto.ReservationDTO;
import com.ziyara.dto.VoyageBriefDTO;
import com.ziyara.model.Reservation;
import com.ziyara.model.Utilisateur;
import com.ziyara.model.Voyage;

import java.util.List;

public class ReservationMapper {

    public static ReservationDTO toDTO(Reservation r) {
        Voyage v = r.getVoyage();

        VoyageBriefDTO voyageDTO = new VoyageBriefDTO(
                v.getId(),
                v.getTitre(),
                v.getRegion(),
                v.getPrix(),
                v.getDateDepart(),
                v.getDateRetour(),
                v.getImageUrl()
        );

        // Defensive check to prevent NullPointerException
        String statut = (r.getStatut() != null)
                ? r.getStatut().name()
                : "EN_ATTENTE";

        return new ReservationDTO(
                r.getId(),
                r.getDateReservation(),
                r.getNombrePersonnes(),
                statut,
                voyageDTO
        );
    }
    public static ReservationAdminDTO toAdminDTO(Reservation r) {
        Voyage v = r.getVoyage();
        Utilisateur u = r.getUtilisateur();
        VoyageBriefDTO voyageDTO = new VoyageBriefDTO(
                v.getId(), v.getTitre(), v.getRegion(), v.getPrix(),
                v.getDateDepart(), v.getDateRetour(), v.getImageUrl()
        );
        String statut = (r.getStatut() != null) ? r.getStatut().name() : "EN_ATTENTE";
        return new ReservationAdminDTO(
                r.getId(),
                r.getDateReservation(),
                r.getNombrePersonnes(),
                statut,
                u.getNom(),
                u.getPrenom(),
                u.getEmail(),
                voyageDTO
        );
    }
    public static List<ReservationDTO> toDTOs(List<Reservation> list) {
        return list.stream()
                .map(ReservationMapper::toDTO)
                .toList();
    }
    public static List<ReservationAdminDTO> toAdminDTOs(List<Reservation> list) {
        return list.stream().map(ReservationMapper::toAdminDTO).toList();
    }
}
