package com.ziyara.mappers;

import com.ziyara.dto.AvisDTO;
import com.ziyara.model.Avis;
import com.ziyara.model.Utilisateur;

import java.util.List;

public class AvisMapper {

    public static AvisDTO toDTO(Avis a) {
        Utilisateur u = a.getUtilisateur();
        return new AvisDTO(
                a.getId(),
                a.getNote(),
                a.getCommentaire(),
                a.getDateAvis(),
                u.getNom(),
                u.getPrenom(),
                u.getEmail()
        );
    }

    public static List<AvisDTO> toDTOs(List<Avis> avisList) {
        return avisList.stream().map(AvisMapper::toDTO).toList();
    }
}
