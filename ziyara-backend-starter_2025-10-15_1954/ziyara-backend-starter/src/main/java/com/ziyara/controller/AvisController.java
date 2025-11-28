package com.ziyara.controller;

import com.ziyara.dto.AvisCreateRequest;
import com.ziyara.dto.AvisDTO;
import com.ziyara.mappers.AvisMapper;
import com.ziyara.model.Avis;
import com.ziyara.service.AvisService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avis")
@CrossOrigin(origins = "*")
public class AvisController {

    private final AvisService service;

    public AvisController(AvisService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @PostMapping
    public ResponseEntity<AvisDTO> createOrUpdate(Authentication auth,
                                                  @RequestBody AvisCreateRequest req) {
        Avis avis = service.createOrUpdate(auth.getName(), req.voyageId(), req.note(), req.commentaire());
        return ResponseEntity.status(200).body(AvisMapper.toDTO(avis));
    }

    @Operation(summary = "List all reviews for a given voyage")
    @GetMapping("/voyage/{voyageId}")
    public ResponseEntity<List<AvisDTO>> list(@PathVariable("voyageId") Long voyageId) {
        List<Avis> avis = service.byVoyage(voyageId);
        return ResponseEntity.ok(AvisMapper.toDTOs(avis));
    }

    @Operation(summary = "Get average rating of a voyage")
    @GetMapping("/voyage/{voyageId}/average")
    public ResponseEntity<Double> average(@PathVariable("voyageId") Long voyageId) {
        return ResponseEntity.ok(service.averageRating(voyageId));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyAvis(Authentication auth, @PathVariable("id") Long id) {
        service.deleteOwned(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
