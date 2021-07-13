package com.hybrid.libraryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybrid.libraryproject.dto.BookCUDTO;
import com.hybrid.libraryproject.model.Book;
import com.hybrid.libraryproject.repository.BookRepository;
import com.hybrid.libraryproject.security.AuthenticationRequest;
import com.hybrid.libraryproject.security.AuthenticationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository repository;

    private String accessToken;

    public static Integer pageNumber = 0;

    @BeforeEach
    public void login() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest("pera@gmail.com", "pera");

        MvcResult result = mvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))).andReturn();

        String resultJson = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(resultJson, AuthenticationResponse.class);
        accessToken = "Bearer " + response.getAccessToken();
    }

    @Test
    public void testMvcGetAllSuccess() throws Exception {

        MvcResult result = mvc.perform(get("/api/books/{pageNumber}", pageNumber)
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect((jsonPath("$", hasSize(3)))).andReturn();

        String resultJson = result.getResponse().getContentAsString();
        List<Book> returnedBooks = List.of(objectMapper.readValue(resultJson, Book[].class));
        List<Book> books = repository.findAll();

        Assertions.assertTrue(books.containsAll(returnedBooks));
    }

    @Test
    public void testMvcGetAllFail() throws Exception {

        mvc.perform(get("/api/books/{pageNumber}", pageNumber)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", not(0))));
    }

   /* @Test
    public void testMvcGetSuccess() throws Exception {

        Long id = 1L;

        MvcResult result = mvc.perform(get("/api/books/{id}", id)
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        Book returnedBook = objectMapper.readValue(resultJson, Book.class);
        Optional<Book> book = repository.findById(id);

        assertEquals(book.get(), returnedBook);
    }

    @Test
    public void testMvcGetEntityNotFound() throws Exception {

        Long id = 10L;

        mvc.perform(get("/api/books/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }*/

    @Test
    public void testMvcCreateSuccess() throws Exception {

        List<Long> authors = List.of(2L);
        BookCUDTO bookDTO = new BookCUDTO("Title","", "1234", LocalDate.now(), 10, 0, authors);

        MvcResult result = mvc.perform(post("/api/books")
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookDTO)))
                                .andExpect(status().isOk())
                                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        Book returnedBook = objectMapper.readValue(resultJson, Book.class);
        
        assertEquals(bookDTO.getTitle(), returnedBook.getTitle());
        assertEquals(bookDTO.getIsbn(), returnedBook.getIsbn());
        assertEquals(bookDTO.getCreationDate(), returnedBook.getCreationDate());
        assertEquals(bookDTO.getCopiesTotal(), returnedBook.getCopiesTotal());

    }

    @Test
    public void testMvcCreateBadRequest() throws Exception {

        List<Long> authors = List.of(2L);
        BookCUDTO bookDTO = new BookCUDTO(null, "", "1234", LocalDate.now(), 10, 0, authors);

        mvc.perform(post("/api/books")
                    .header("Authorization", accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bookDTO)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testMvcUpdateSuccess() throws Exception {

        List<Long> authors = List.of(2L);
        BookCUDTO bookDTO = new BookCUDTO("Title Changed", "", "12345", LocalDate.now(), 10, 0, authors);
        Long bookId = 1L;

        MvcResult result = mvc.perform(put("/api/books/{id}", bookId)
                                .header("Authorization", accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookDTO)))
                                .andExpect(status().isOk())
                                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        Book returnedBook = objectMapper.readValue(resultJson, Book.class);

        assertEquals(bookDTO.getTitle(), returnedBook.getTitle());
        assertEquals(bookDTO.getIsbn(), returnedBook.getIsbn());
        assertEquals(bookDTO.getCreationDate(), returnedBook.getCreationDate());
        assertEquals(bookDTO.getCopiesTotal(), returnedBook.getCopiesTotal());
    }

    @Test
    public void testMvcUpdateEntityNotFound() throws Exception {

        List<Long> authors = List.of(2L);
        BookCUDTO bookDTO = new BookCUDTO("Title 1", "", "1234", LocalDate.now(), 10, 0, authors);
        Long id = 10L;

        mvc.perform(put("/api/books/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMvcDeleteSuccess() throws Exception {

        Long id = 3L;

        mvc.perform(delete("/api/books/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testMvcDeleteEntityNotFound() throws Exception {

        Long id = 10L;

        mvc.perform(delete("/api/books/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
