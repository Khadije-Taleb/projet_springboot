package com.smartlibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "membres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroAdherent;

    @Column(nullable = false)
    private String nomComplet;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    private String adresse;

    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
