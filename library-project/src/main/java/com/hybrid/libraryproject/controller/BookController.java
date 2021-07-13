package com.hybrid.libraryproject.controller;

import com.hybrid.libraryproject.dto.BookCUDTO;
import com.hybrid.libraryproject.dto.BookRDTO;
import com.hybrid.libraryproject.mapper.BookMapper;
import com.hybrid.libraryproject.model.Book;
import com.hybrid.libraryproject.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/books")
@Slf4j
public class BookController {

    private final BookService service;

    private final BookMapper mapper;

    BookController(BookService bookService, BookMapper bookMapper) {
        this.service = bookService;
        this.mapper = bookMapper;
    }

    @Operation(summary = "Search all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning all books that match criteria",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = BookRDTO.class))) }) })
    @GetMapping(value = "/{pageNumber}")
    public ResponseEntity<List<BookRDTO>> getAll(@Parameter(description = "Page number")
                                                         @PathVariable Integer pageNumber,
                                                    @RequestParam Map<String, String> queryParams){

        Page<Book> found = service.getAll(queryParams, pageNumber);
        return ResponseEntity.ok().body(found.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Create a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book created",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content) })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookRDTO> create(@Valid @RequestBody BookCUDTO bookDTO){

        Book created = service.create(bookDTO);
        log.info("Book with id {} created", created.getId());
        return ResponseEntity.ok().body(mapper.toDTO(created));
    }

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = { @Content( mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookRDTO> update(@Parameter(description = "Id of book to be updated")
                                                @PathVariable Long id,
                                                @Valid @RequestBody BookCUDTO bookDTO) {

        Book updated = service.update(id, bookDTO);
        log.info("Book with id {} updated", id);
        return ResponseEntity.ok().body(mapper.toDTO(updated));
    }

    @Operation(summary = "Delete a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted",
                    content = @Content ),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Id of book to be deleted")
                                        @PathVariable Long id) {

        service.delete(id);
        log.info("Book with id {} deleted", id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Upload image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded",
                    content = @Content ),
            @ApiResponse(responseCode = "404", description = "Failed uploading image",
                    content = @Content) })
    @PostMapping(value = "/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {

        return ResponseEntity.ok().body(service.uploadImage(file));
    }

    @Operation(summary = "Get most rented books books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning most rented books",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = BookRDTO.class))) }) })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_READER')")
    @GetMapping(value = "/most-rented/{number}")
    public ResponseEntity<List<BookRDTO>> getMostRentedBooks(@Parameter(description = "Number of books to be returned")
                                                 @PathVariable Integer number){

        List<Book> found = service.getMostRented(number);
        return ResponseEntity.ok().body(found.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }
}
