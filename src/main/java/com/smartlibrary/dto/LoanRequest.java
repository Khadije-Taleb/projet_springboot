package com.smartlibrary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequest {
    @NotNull(message = "L'ID du membre est obligatoire")
    private Long membreId;

    @NotNull(message = "L'ID du livre est obligatoire")
    private Long livreId;
}
