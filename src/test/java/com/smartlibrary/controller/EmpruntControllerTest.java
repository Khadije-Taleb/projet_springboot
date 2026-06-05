package com.smartlibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlibrary.dto.EmpruntDTO;
import com.smartlibrary.dto.LoanRequest;
import com.smartlibrary.entity.StatutEmprunt;
import com.smartlibrary.service.EmpruntService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpruntController.class)
class EmpruntControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpruntService empruntService;

    @Test
    void getAllLoans_returnsList() throws Exception {
        EmpruntDTO loan = new EmpruntDTO();
        loan.setId(1L);
        loan.setDateEmprunt(LocalDate.now());
        loan.setDateRetourPrevue(LocalDate.now().plusDays(14));
        loan.setStatut(StatutEmprunt.ACTIF);

        given(empruntService.getAllLoans()).willReturn(List.of(loan));

        mockMvc.perform(get("/api/loans").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statut").value("ACTIF"));
    }

    @Test
    void getLoansByUserId_returnsList() throws Exception {
        EmpruntDTO loan = new EmpruntDTO();
        loan.setId(2L);
        loan.setStatut(StatutEmprunt.ACTIF);

        given(empruntService.getLoansByUserId(1L)).willReturn(List.of(loan));

        mockMvc.perform(get("/api/loans/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void createLoan_returnsCreatedLoan() throws Exception {
        LoanRequest request = new LoanRequest();
        request.setMembreId(1L);
        request.setLivreId(2L);

        EmpruntDTO created = new EmpruntDTO();
        created.setId(3L);
        created.setStatut(StatutEmprunt.ACTIF);
        created.setDateEmprunt(LocalDate.now());
        created.setDateRetourPrevue(LocalDate.now().plusDays(14));

        given(empruntService.createLoan(any(LoanRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.statut").value("ACTIF"));
    }

    @Test
    void returnLoan_returnsUpdatedLoan() throws Exception {
        EmpruntDTO returned = new EmpruntDTO();
        returned.setId(4L);
        returned.setStatut(StatutEmprunt.RETOURNE);
        returned.setDateRetourEffective(LocalDate.now());

        given(empruntService.returnLoan(4L)).willReturn(returned);

        mockMvc.perform(put("/api/loans/4/return")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.statut").value("RETOURNE"));
    }
}
