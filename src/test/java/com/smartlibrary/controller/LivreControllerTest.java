package com.smartlibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlibrary.dto.BookRequest;
import com.smartlibrary.dto.LivreDTO;
import com.smartlibrary.service.LivreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivreController.class)
class LivreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivreService livreService;

    @Test
    void getAllBooks_returnsList() throws Exception {
        LivreDTO book = new LivreDTO();
        book.setId(1L);
        book.setIsbn("9781234567897");
        book.setTitre("Test Livre");
        book.setAuteur("Auteur Test");
        book.setCategorie("Fiction");
        book.setDescription("Description de test");
        book.setImageCouverture("http://image.test");
        book.setExemplairesTotal(3);
        book.setExemplairesDisponibles(3);

        given(livreService.getAllBooks()).willReturn(List.of(book));

        mockMvc.perform(get("/api/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titre").value("Test Livre"));
    }

    @Test
    void getBookById_returnsBook() throws Exception {
        LivreDTO book = new LivreDTO();
        book.setId(1L);
        book.setIsbn("9781234567897");
        book.setTitre("Livre unique");

        given(livreService.getBookById(1L)).willReturn(book);

        mockMvc.perform(get("/api/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Livre unique"));
    }

    @Test
    void createBook_returnsCreatedBook() throws Exception {
        BookRequest request = new BookRequest();
        request.setIsbn("9781234567897");
        request.setTitre("Nouveau Livre");
        request.setAuteur("Auteur Test");
        request.setCategorie("Science");
        request.setDescription("Desc");
        request.setImageCouverture("http://image.test");
        request.setExemplairesTotal(5);

        LivreDTO created = new LivreDTO();
        created.setId(2L);
        created.setIsbn(request.getIsbn());
        created.setTitre(request.getTitre());
        created.setAuteur(request.getAuteur());
        created.setCategorie(request.getCategorie());
        created.setDescription(request.getDescription());
        created.setImageCouverture(request.getImageCouverture());
        created.setExemplairesTotal(request.getExemplairesTotal());
        created.setExemplairesDisponibles(request.getExemplairesTotal());

        given(livreService.createBook(any(BookRequest.class))).willReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.titre").value("Nouveau Livre"));
    }

    @Test
    void updateBook_returnsUpdatedBook() throws Exception {
        BookRequest request = new BookRequest();
        request.setIsbn("9781234567897");
        request.setTitre("Livre Modifié");
        request.setAuteur("Auteur Test");
        request.setCategorie("Science");
        request.setDescription("Desc modifiée");
        request.setImageCouverture("http://image.test");
        request.setExemplairesTotal(4);

        LivreDTO updated = new LivreDTO();
        updated.setId(1L);
        updated.setTitre(request.getTitre());
        updated.setExemplairesTotal(request.getExemplairesTotal());

        given(livreService.updateBook(eq(1L), any(BookRequest.class))).willReturn(updated);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Livre Modifié"));
    }

    @Test
    void deleteBook_returnsOk() throws Exception {
        mockMvc.perform(delete("/api/books/1")).andExpect(status().isOk());
    }

    @Test
    void searchBooks_returnsMatchingBooks() throws Exception {
        LivreDTO book = new LivreDTO();
        book.setId(3L);
        book.setTitre("Recherche Livre");
        given(livreService.searchBooks("Recherche")).willReturn(List.of(book));

        mockMvc.perform(get("/api/books/search").param("keyword", "Recherche").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].titre").value("Recherche Livre"));
    }
}
