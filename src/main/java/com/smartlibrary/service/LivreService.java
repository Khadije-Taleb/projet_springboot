package com.smartlibrary.service;

import com.smartlibrary.dto.BookRequest;
import com.smartlibrary.dto.LivreDTO;
import com.smartlibrary.entity.Livre;
import com.smartlibrary.exception.BadRequestException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.repository.LivreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepository livreRepository;

    public List<LivreDTO> getAllBooks() {
        return livreRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public LivreDTO getBookById(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
        return mapToDTO(livre);
    }

    public LivreDTO createBook(BookRequest request) {
        // Optional: verify if ISBN already exists
        Livre livre = Livre.builder()
                .isbn(request.getIsbn())
                .titre(request.getTitre())
                .auteur(request.getAuteur())
                .categorie(request.getCategorie())
                .description(request.getDescription())
                .imageCouverture(request.getImageCouverture())
                .exemplairesTotal(request.getExemplairesTotal())
                .exemplairesDisponibles(request.getExemplairesTotal())
                .build();

        return mapToDTO(livreRepository.save(livre));
    }

    public LivreDTO updateBook(Long id, BookRequest request) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));

        int diff = request.getExemplairesTotal() - livre.getExemplairesTotal();
        int newDisponibles = livre.getExemplairesDisponibles() + diff;
        
        if (newDisponibles < 0) {
            throw new BadRequestException("Le nombre total d'exemplaires ne peut pas être inférieur au nombre de livres actuellement empruntés.");
        }

        livre.setIsbn(request.getIsbn());
        livre.setTitre(request.getTitre());
        livre.setAuteur(request.getAuteur());
        livre.setCategorie(request.getCategorie());
        livre.setDescription(request.getDescription());
        livre.setImageCouverture(request.getImageCouverture());
        livre.setExemplairesTotal(request.getExemplairesTotal());
        livre.setExemplairesDisponibles(newDisponibles);

        return mapToDTO(livreRepository.save(livre));
    }

    public void deleteBook(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'id : " + id));
        livreRepository.delete(livre);
    }

    public List<LivreDTO> searchBooks(String keyword) {
        return livreRepository.searchByKeyword(keyword).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public LivreDTO mapToDTO(Livre livre) {
        LivreDTO dto = new LivreDTO();
        dto.setId(livre.getId());
        dto.setIsbn(livre.getIsbn());
        dto.setTitre(livre.getTitre());
        dto.setAuteur(livre.getAuteur());
        dto.setCategorie(livre.getCategorie());
        dto.setDescription(livre.getDescription());
        dto.setImageCouverture(livre.getImageCouverture());
        dto.setExemplairesTotal(livre.getExemplairesTotal());
        dto.setExemplairesDisponibles(livre.getExemplairesDisponibles());
        return dto;
    }
}
