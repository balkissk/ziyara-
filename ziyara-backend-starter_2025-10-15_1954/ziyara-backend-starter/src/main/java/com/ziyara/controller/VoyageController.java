package com.ziyara.controller;

import com.ziyara.model.Voyage;
import com.ziyara.service.VoyageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/voyages")
@CrossOrigin(origins = "*")
public class VoyageController {

    private final VoyageService service;
    public VoyageController(VoyageService service){ this.service = service; }

    @GetMapping
    public List<Voyage> list(){ return service.list(); }

    @Operation(summary = "Get a voyage by ID", description = "Retrieve details of a specific voyage by its ID")
    @GetMapping("/{id}")
    public Voyage get(
            @Parameter(description = "ID du voyage à récupérer", example = "1")
            @PathVariable("id") Long id) {
        return service.get(id);
    }

    @Operation(summary = "Create a voyage", description = "ADMIN only — creates a new voyage")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Voyage create(@RequestBody Voyage v){ return service.save(v); }

    @Operation(summary = "Delete a voyage", description = "ADMIN only — delete a voyage by ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du voyage à supprimer", example = "1")
            @PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a voyage", description = "ADMIN only — partially updates an existing voyage")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Voyage update(
            @Parameter(description = "ID du voyage à mettre à jour", example = "2")
            @PathVariable("id") Long id,
            @RequestBody Voyage v) {
        return service.update(id, v);
    }


}
