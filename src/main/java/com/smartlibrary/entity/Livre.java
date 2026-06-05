package com.smartlibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "livres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String auteur;

    private String categorie;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageCouverture;

    @Column(nullable = false)
    private Integer exemplairesTotal;

    @Column(nullable = false)
    private Integer exemplairesDisponibles;
}
