package com.smartlibrary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "L'ISBN est obligatoire")
    private String isbn;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "L'auteur est obligatoire")
    private String auteur;

    private String categorie;

    private String description;

    private String imageCouverture;

    @NotNull(message = "Le nombre total d'exemplaires est obligatoire")
    @Min(value = 1, message = "Il faut au moins 1 exemplaire")
    private Integer exemplairesTotal;
}
