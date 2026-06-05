package com.smartlibrary.repository;

import com.smartlibrary.entity.Membre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembreRepository extends JpaRepository<Membre, Long> {
    Optional<Membre> findByEmail(String email);
    Optional<Membre> findByNumeroAdherent(String numeroAdherent);
}
