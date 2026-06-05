package com.smartlibrary.service;

import com.smartlibrary.dto.AuthResponse;
import com.smartlibrary.dto.LoginRequest;
import com.smartlibrary.dto.MembreDTO;
import com.smartlibrary.dto.RegisterRequest;
import com.smartlibrary.entity.Membre;
import com.smartlibrary.entity.Role;
import com.smartlibrary.exception.BadRequestException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.repository.MembreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembreService {

    private final MembreRepository membreRepository;

    public AuthResponse register(RegisterRequest request) {
        if (membreRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Un membre avec cet email existe déjà.");
        }
        if (membreRepository.findByNumeroAdherent(request.getNumeroAdherent()).isPresent()) {
            throw new BadRequestException("Ce numéro d'adhérent est déjà utilisé.");
        }

        Membre membre = Membre.builder()
                .numeroAdherent(request.getNumeroAdherent())
                .nomComplet(request.getNomComplet())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .adresse(request.getAdresse())
                .motDePasse(request.getMotDePasse()) // Pas de hashage pour l'instant
                .role(Role.ROLE_MEMBRE)
                .build();

        Membre savedMembre = membreRepository.save(membre);
        return new AuthResponse("Inscription réussie", mapToDTO(savedMembre));
    }

    public AuthResponse login(LoginRequest request) {
        Membre membre = membreRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Identifiants incorrects."));

        if (!membre.getMotDePasse().equals(request.getMotDePasse())) {
            throw new BadRequestException("Mot de passe incorrect.");
        }

        return new AuthResponse("Connexion réussie", mapToDTO(membre));
    }

    public MembreDTO getMembreById(Long id) {
        Membre membre = membreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé avec l'id : " + id));
        return mapToDTO(membre);
    }

    private MembreDTO mapToDTO(Membre membre) {
        MembreDTO dto = new MembreDTO();
        dto.setId(membre.getId());
        dto.setNumeroAdherent(membre.getNumeroAdherent());
        dto.setNomComplet(membre.getNomComplet());
        dto.setEmail(membre.getEmail());
        dto.setTelephone(membre.getTelephone());
        dto.setAdresse(membre.getAdresse());
        dto.setRole(membre.getRole());
        return dto;
    }
}
