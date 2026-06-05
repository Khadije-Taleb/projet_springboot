package com.smartlibrary.service;

import com.smartlibrary.dto.EmpruntDTO;
import com.smartlibrary.dto.LoanRequest;
import com.smartlibrary.entity.Emprunt;
import com.smartlibrary.entity.Livre;
import com.smartlibrary.entity.Membre;
import com.smartlibrary.entity.StatutEmprunt;
import com.smartlibrary.exception.BadRequestException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.repository.EmpruntRepository;
import com.smartlibrary.repository.LivreRepository;
import com.smartlibrary.repository.MembreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpruntService {

    private final EmpruntRepository empruntRepository;
    private final MembreRepository membreRepository;
    private final LivreRepository livreRepository;
    private final LivreService livreService;

    public List<EmpruntDTO> getAllLoans() {
        return empruntRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<EmpruntDTO> getLoansByUserId(Long userId) {
        return empruntRepository.findByMembreId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public EmpruntDTO createLoan(LoanRequest request) {
        Membre membre = membreRepository.findById(request.getMembreId())
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé."));
        
        Livre livre = livreRepository.findById(request.getLivreId())
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé."));

        if (livre.getExemplairesDisponibles() <= 0) {
            throw new BadRequestException("Aucun exemplaire disponible pour ce livre.");
        }

        livre.setExemplairesDisponibles(livre.getExemplairesDisponibles() - 1);
        livreRepository.save(livre);

        Emprunt emprunt = Emprunt.builder()
                .dateEmprunt(LocalDate.now())
                .dateRetourPrevue(LocalDate.now().plusDays(14)) // Emprunt de 14 jours par défaut
                .statut(StatutEmprunt.ACTIF)
                .membre(membre)
                .livre(livre)
                .build();

        return mapToDTO(empruntRepository.save(emprunt));
    }

    @Transactional
    public EmpruntDTO returnLoan(Long loanId) {
        Emprunt emprunt = empruntRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé."));

        if (emprunt.getStatut() == StatutEmprunt.RETOURNE) {
            throw new BadRequestException("Cet emprunt a déjà été retourné.");
        }

        emprunt.setDateRetourEffective(LocalDate.now());
        emprunt.setStatut(StatutEmprunt.RETOURNE);

        Livre livre = emprunt.getLivre();
        livre.setExemplairesDisponibles(livre.getExemplairesDisponibles() + 1);
        livreRepository.save(livre);

        return mapToDTO(empruntRepository.save(emprunt));
    }

    private EmpruntDTO mapToDTO(Emprunt emprunt) {
        EmpruntDTO dto = new EmpruntDTO();
        dto.setId(emprunt.getId());
        dto.setDateEmprunt(emprunt.getDateEmprunt());
        dto.setDateRetourPrevue(emprunt.getDateRetourPrevue());
        dto.setDateRetourEffective(emprunt.getDateRetourEffective());
        dto.setStatut(emprunt.getStatut());

        Membre membre = emprunt.getMembre();
        com.smartlibrary.dto.MembreDTO membreDTO = new com.smartlibrary.dto.MembreDTO();
        membreDTO.setId(membre.getId());
        membreDTO.setNumeroAdherent(membre.getNumeroAdherent());
        membreDTO.setNomComplet(membre.getNomComplet());
        membreDTO.setEmail(membre.getEmail());
        membreDTO.setTelephone(membre.getTelephone());
        membreDTO.setAdresse(membre.getAdresse());
        membreDTO.setRole(membre.getRole());
        dto.setMembre(membreDTO);

        dto.setLivre(livreService.mapToDTO(emprunt.getLivre()));

        return dto;
    }
}
