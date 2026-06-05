package com.smartlibrary.repository;

import com.smartlibrary.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivreRepository extends JpaRepository<Livre, Long> {
    @Query("SELECT l FROM Livre l WHERE " +
            "LOWER(l.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.auteur) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.categorie) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Livre> searchByKeyword(String keyword);
}
