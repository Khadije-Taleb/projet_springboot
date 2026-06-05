package com.smartlibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlibrary.dto.AuthResponse;
import com.smartlibrary.dto.LoginRequest;
import com.smartlibrary.dto.MembreDTO;
import com.smartlibrary.dto.RegisterRequest;
import com.smartlibrary.entity.Role;
import com.smartlibrary.service.MembreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MembreService membreService;

    @Test
    void register_returnsCreatedAuthResponse() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNumeroAdherent("A123");
        request.setNomComplet("Jean Dupont");
        request.setEmail("jean@test.com");
        request.setTelephone("0123456789");
        request.setAdresse("1 rue de Test");
        request.setMotDePasse("password");

        MembreDTO membreDTO = new MembreDTO();
        membreDTO.setId(1L);
        membreDTO.setNumeroAdherent(request.getNumeroAdherent());
        membreDTO.setNomComplet(request.getNomComplet());
        membreDTO.setEmail(request.getEmail());
        membreDTO.setTelephone(request.getTelephone());
        membreDTO.setAdresse(request.getAdresse());
        membreDTO.setRole(Role.ROLE_MEMBRE);

        AuthResponse response = new AuthResponse("Inscription réussie", membreDTO);
        given(membreService.register(any(RegisterRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Inscription réussie"))
                .andExpect(jsonPath("$.membre.email").value("jean@test.com"));
    }

    @Test
    void login_returnsAuthResponse() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("jean@test.com");
        request.setMotDePasse("password");

        MembreDTO membreDTO = new MembreDTO();
        membreDTO.setId(1L);
        membreDTO.setEmail(request.getEmail());
        membreDTO.setNomComplet("Jean Dupont");
        membreDTO.setRole(Role.ROLE_MEMBRE);

        AuthResponse response = new AuthResponse("Connexion réussie", membreDTO);
        given(membreService.login(any(LoginRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Connexion réussie"))
                .andExpect(jsonPath("$.membre.email").value("jean@test.com"));
    }
}
