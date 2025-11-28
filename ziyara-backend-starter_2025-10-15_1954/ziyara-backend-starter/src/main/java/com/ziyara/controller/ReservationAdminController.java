package com.ziyara.controller;

import com.ziyara.model.Reservation;
import com.ziyara.service.AdminReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final AdminReservationService adminReservationService;

    /** PATCH /api/admin/reservations/{id}/confirm */
    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public Reservation confirm(@PathVariable("id") Long id) {
        return adminReservationService.confirm(id);
    }

    /** PATCH /api/admin/reservations/{id}/cancel */
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public Reservation cancel(@PathVariable("id") Long id) {
        return adminReservationService.cancel(id);
    }
}
