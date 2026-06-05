package com.smartlibrary.controller;

import com.smartlibrary.dto.EmpruntDTO;
import com.smartlibrary.dto.LoanRequest;
import com.smartlibrary.service.EmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class EmpruntController {

    private final EmpruntService empruntService;

    @GetMapping
    public ResponseEntity<List<EmpruntDTO>> getAllLoans() {
        return ResponseEntity.ok(empruntService.getAllLoans());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<EmpruntDTO>> getLoansByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(empruntService.getLoansByUserId(id));
    }

    @PostMapping
    public ResponseEntity<EmpruntDTO> createLoan(@Valid @RequestBody LoanRequest request) {
        return new ResponseEntity<>(empruntService.createLoan(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<EmpruntDTO> returnLoan(@PathVariable Long id) {
        return ResponseEntity.ok(empruntService.returnLoan(id));
    }
}
