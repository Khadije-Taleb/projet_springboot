package com.smartlibrary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Le numéro d'adhérent est obligatoire")
    private String numeroAdherent;
    
    @NotBlank(message = "Le nom complet est obligatoire")
    private String nomComplet;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    private String telephone;
    
    private String adresse;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
}
