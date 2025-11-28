package com.ziyara.dto;

import com.ziyara.model.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Utilisateur user;
}
