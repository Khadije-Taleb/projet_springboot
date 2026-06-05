package com.smartlibrary.dto;

import com.smartlibrary.entity.StatutEmprunt;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmpruntDTO {
    private Long id;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private StatutEmprunt statut;
    private MembreDTO membre;
    private LivreDTO livre;
}
