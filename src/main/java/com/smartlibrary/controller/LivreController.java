package com.smartlibrary.controller;

import com.smartlibrary.dto.BookRequest;
import com.smartlibrary.dto.LivreDTO;
import com.smartlibrary.service.LivreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class LivreController {

    private final LivreService livreService;

    @GetMapping
    public ResponseEntity<List<LivreDTO>> getAllBooks() {
        return ResponseEntity.ok(livreService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(livreService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<LivreDTO> createBook(@Valid @RequestBody BookRequest request) {
        return new ResponseEntity<>(livreService.createBook(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(livreService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        livreService.deleteBook(id);
        return ResponseEntity.ok("Livre supprimé avec succès.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<LivreDTO>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(livreService.searchBooks(keyword));
    }
}
