package com.hybrid.libraryproject.controller;

import com.hybrid.libraryproject.dto.BookRentalCUDTO;
import com.hybrid.libraryproject.dto.BookRentalRDTO;
import com.hybrid.libraryproject.mapper.BookRentalMapper;
import com.hybrid.libraryproject.model.BookRental;
import com.hybrid.libraryproject.service.BookRentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/book-rentals")
@Slf4j
public class BookRentalController {

    private final BookRentalService service;

    private final BookRentalMapper mapper;

    public BookRentalController(BookRentalService bookRentalService, BookRentalMapper bookRentalMapper) {
        this.service = bookRentalService;
        this.mapper = bookRentalMapper;
    }

    @Operation(summary = "Get all book rentals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning all books rentals",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = BookRentalRDTO.class))) }) })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookRentalRDTO>> getAll(){

        List<BookRental> found = service.getAll();
        return ResponseEntity.ok().body(found.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Get a book rental by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book rental found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookRentalRDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Book rental not found",
                    content = @Content) })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_READER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BookRentalRDTO> get(@Parameter(description = "Id of book rental to be searched")
                                        @PathVariable Long id) {

        BookRental found = service.get(id);
        return ResponseEntity.ok().body(mapper.toDTO(found));
    }

    @Operation(summary = "Create book rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book rental created",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookRentalCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content) })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_READER')")
    @PostMapping
    public ResponseEntity<BookRentalRDTO> create(@Valid @RequestBody BookRentalCUDTO bookRentalDTO){

        BookRental created = service.create(bookRentalDTO);
        log.info("Book rental with id {} created", created.getId());
        return ResponseEntity.ok().body(mapper.toDTO(created));
    }

    @Operation(summary = "Update book rental with return date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book rental updated",
                    content = { @Content( mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookRentalCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book rental not found",
                    content = @Content) })
    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_READER')")
    public ResponseEntity<BookRentalRDTO> update(@Parameter(description = "Id of book rental to be updated")
                                                 @PathVariable Long id) {

        BookRental updated = service.update(id);
        log.info("Book rental with id {} returned", id);
        return ResponseEntity.ok().body(mapper.toDTO(updated));
    }

    @Operation(summary = "Delete a book rental by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book rental deleted",
                    content = @Content ),
            @ApiResponse(responseCode = "404", description = "Book rental not found",
                    content = @Content) })
    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Id of book rental to be deleted")
                                       @PathVariable Long id) {

        service.delete(id);
        log.info("Book rental with id {} deleted", id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all overdue book returns")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning all overdue book returns",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = BookRentalRDTO.class))) }) })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/overdue-book-returns")
    public ResponseEntity<List<BookRentalRDTO>> getOverdueBookReturns(){

        List<BookRental> found = service.getAllOverdueBookReturns();
        return ResponseEntity.ok().body(found.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

}
