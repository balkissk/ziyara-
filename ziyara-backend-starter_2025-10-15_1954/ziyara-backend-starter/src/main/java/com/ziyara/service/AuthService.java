package com.ziyara.service;

import com.ziyara.model.Utilisateur;
import com.ziyara.repository.UtilisateurRepository;
import com.ziyara.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtilisateurRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository userRepo, PasswordEncoder encoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public Utilisateur register(Utilisateur u){
        u.setMotDePasse(encoder.encode(u.getMotDePasse()));
        return userRepo.save(u);
    }

    public String login(String email, String rawPassword) {
        Utilisateur u = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe invalide"));

        if (!encoder.matches(rawPassword, u.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe invalide");
        }

        return jwtService.generateToken(u);
    }
}
