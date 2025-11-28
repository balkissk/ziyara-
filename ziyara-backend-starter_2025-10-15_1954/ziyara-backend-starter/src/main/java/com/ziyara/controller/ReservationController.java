package com.ziyara.controller;

import com.ziyara.dto.ReservationAdminDTO;
import com.ziyara.dto.ReservationDTO;
import com.ziyara.mappers.ReservationMapper;
import com.ziyara.model.Reservation;
import com.ziyara.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create a reservation",
            description = "CLIENT or ADMIN — creates a reservation for the current authenticated user"
    )
    @PreAuthorize("hasAnyRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ReservationDTO> create(
            Authentication auth,
            @RequestParam("voyageId") Long voyageId,
            @RequestParam("personnes") int personnes) {

        Reservation reservation = service.createForUser(auth.getName(), voyageId, personnes);
        ReservationDTO dto = ReservationMapper.toDTO(reservation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto);
    }


    @Operation(
            summary = "List reservations of current user",
            description = "CLIENT or ADMIN — get all reservations of the logged-in user"
    )
    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<ReservationDTO>> myReservations(Authentication auth) {
        List<Reservation> reservations = service.byUserEmail(auth.getName());
        List<ReservationDTO> dtos = ReservationMapper.toDTOs(reservations);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "List all reservations (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationAdminDTO>> allReservations() {
        List<Reservation> list = service.allReservations();
        return ResponseEntity.ok(ReservationMapper.toAdminDTOs(list));
    }

    @Operation(summary = "Delete reservation by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
