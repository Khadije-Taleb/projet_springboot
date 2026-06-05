package com.smartlibrary.dto;

import com.smartlibrary.entity.Role;
import lombok.Data;

@Data
public class MembreDTO {
    private Long id;
    private String numeroAdherent;
    private String nomComplet;
    private String email;
    private String telephone;
    private String adresse;
    private Role role;
}
