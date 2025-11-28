package com.ziyara.controller;

import com.ziyara.dto.LoginRequest;
import com.ziyara.dto.LoginResponse;
import com.ziyara.model.Utilisateur;
import com.ziyara.repository.UtilisateurRepository;
import com.ziyara.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UtilisateurRepository userRepo;

    public AuthController(AuthService authService, UtilisateurRepository userRepo) {
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public Utilisateur register(@RequestBody Utilisateur u) {
        return authService.register(u);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());

        Utilisateur user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable après authentification"));

        user.setMotDePasse(null);
        return new LoginResponse(token, user);
    }

    @GetMapping("/me")
    public Utilisateur me(Authentication auth) {
        Utilisateur u = userRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
        u.setMotDePasse(null);
        return u;
    }

}
