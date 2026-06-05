package com.smartlibrary.dto;

import lombok.Data;

@Data
public class LivreDTO {
    private Long id;
    private String isbn;
    private String titre;
    private String auteur;
    private String categorie;
    private String description;
    private String imageCouverture;
    private Integer exemplairesTotal;
    private Integer exemplairesDisponibles;
}
