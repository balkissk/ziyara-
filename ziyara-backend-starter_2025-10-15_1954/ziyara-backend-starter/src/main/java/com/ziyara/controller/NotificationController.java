package com.ziyara.controller;

import com.ziyara.model.Notification;
import com.ziyara.model.Utilisateur;
import com.ziyara.repository.NotificationRepository;
import com.ziyara.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepo;
    private final UtilisateurRepository utilisateurRepo;

    private Utilisateur currentUser(Authentication auth) {
        String email = auth.getName(); // same as in AvisController
        return utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + email));
    }

    @Operation(summary = "List my notifications (paged, newest first)")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Notification>> listMine(Authentication auth,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size",defaultValue = "20") int size) {
        Utilisateur me = currentUser(auth);
        Page<Notification> result = notificationRepo
                .findByUtilisateur_IdOrderByDateEnvoiDesc(me.getId(), PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Delete ONE of my notifications")
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(Authentication auth, @PathVariable("id") Long id) {
        Utilisateur me = currentUser(auth);
        Notification n = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification introuvable: " + id));

        if (!n.getUtilisateur().getId().equals(me.getId())) {
            throw new RuntimeException("Interdit: cette notification n'appartient pas à l'utilisateur connecté.");
        }
        notificationRepo.delete(n);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete ALL my notifications")
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping
    public ResponseEntity<Long> deleteAll(Authentication auth) {
        Utilisateur me = currentUser(auth);
        long deleted = notificationRepo.deleteByUtilisateur_Id(me.getId());
        return ResponseEntity.ok(deleted);
    }
}
