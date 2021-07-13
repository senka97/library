package com.hybrid.libraryproject.controller;

import com.hybrid.libraryproject.dto.AuthorCUDTO;
import com.hybrid.libraryproject.dto.AuthorRDTO;
import com.hybrid.libraryproject.mapper.AuthorMapper;
import com.hybrid.libraryproject.model.Author;
import com.hybrid.libraryproject.service.AuthorService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/authors")
@Slf4j
public class AuthorController {

    private final AuthorService service;

    private final AuthorMapper mapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper){
        this.service = authorService;
        this.mapper = authorMapper;
    }

    @Operation(summary = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning all authors",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AuthorRDTO.class))) }) })
    @GetMapping()
    public ResponseEntity<List<AuthorRDTO>> getAll(){

        List<Author> found = service.getAll();
        return ResponseEntity.ok().body(found.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Create an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author created",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthorCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content) })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<AuthorRDTO> create(@Valid @RequestBody AuthorCUDTO authorDTO){

        Author created = service.create(authorDTO);
        log.info("Author with id {} created", created.getId());
        return ResponseEntity.ok().body(mapper.toDTO(created));
    }

    @Operation(summary = "Update an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated",
                    content = { @Content( mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthorCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content) })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AuthorRDTO> update(@Parameter(description = "Id of author to be updated")
                                           @PathVariable Long id,
                                           @Valid @RequestBody AuthorCUDTO authorDTO) {

        Author updated = service.update(id, authorDTO);
        log.info("Author with id {} updated", id);
        return ResponseEntity.ok().body(mapper.toDTO(updated));
    }

    @Operation(summary = "Delete author by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted",
                    content = @Content ),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content) })
    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Id of author to be deleted")
                                       @PathVariable Long id) {

        service.delete(id);
        log.info("Author with id {} deleted", id);
        return ResponseEntity.ok().build();
    }
}
