package com.hybrid.libraryproject.controller;

import com.hybrid.libraryproject.dto.UserCUDTO;
import com.hybrid.libraryproject.dto.UserRDTO;
import com.hybrid.libraryproject.mapper.UserMapper;
import com.hybrid.libraryproject.model.User;
import com.hybrid.libraryproject.service.UserService;
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
@RequestMapping(value = "/api/users")
@Slf4j
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    public UserController(UserService userService, UserMapper userMapper){
        this.service = userService;
        this.mapper = userMapper;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning all users",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                      array = @ArraySchema(schema = @Schema(implementation = UserRDTO.class))) }) })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRDTO>> getAll(){

        List<User> users = service.getAll();
        return ResponseEntity.ok().body(users.stream().map(mapper::toDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserRDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserRDTO> get(@Parameter(description = "Id of user to be searched")
                                           @PathVariable Long id) {

        User user = service.get(id);
        return ResponseEntity.ok().body(mapper.toDTO(user));
    }

    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content) })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserRDTO> create(@Valid @RequestBody UserCUDTO userDTO){

        User newUser = service.create(userDTO);
        log.info("User with id {} created", newUser.getId());
        return ResponseEntity.ok().body(mapper.toDTO(newUser));
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = { @Content( mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserCUDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserRDTO> update(@Parameter(description = "Id of user to be updated")
                                           @PathVariable Long id,
                                           @Valid @RequestBody UserCUDTO userDTO) {

        User updatedUser = service.update(id, userDTO);
        log.info("User with id {} updated", id);
        return ResponseEntity.ok().body(mapper.toDTO(updatedUser));
    }

    @Operation(summary = "Delete a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @DeleteMapping(value="/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@Parameter(description = "Id of user to be deleted")
                                    @PathVariable Long id) {

        service.delete(id);
        log.info("User with id {} deleted", id);
        return ResponseEntity.ok().build();
    }

}
