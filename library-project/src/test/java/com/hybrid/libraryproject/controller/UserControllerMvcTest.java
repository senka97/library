package com.hybrid.libraryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybrid.libraryproject.dto.UserCUDTO;
import com.hybrid.libraryproject.model.User;
import com.hybrid.libraryproject.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository repository;

    private String accessToken;

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

        MvcResult result = mvc.perform(get("/api/users")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        List<User> returnedUsers = List.of(objectMapper.readValue(resultJson, User[].class));

        List<User> users = repository.findAll();

        Assertions.assertTrue(users.containsAll(returnedUsers));
    }

    @Test
    public void testMvcGetAllFail() throws Exception {

        mvc.perform(get("/api/users")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", not(0))));
    }

    @Test
    public void testMvcGetSuccess() throws Exception {

        Long id = 1L;

        MvcResult result = mvc.perform(get("/api/users/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        User returnedUser = objectMapper.readValue(resultJson, User.class);

        Optional<User> user = repository.findById(id);

        assertEquals(user.get(), returnedUser);

    }

    @Test
    public void testMvcGetEntityNotFound() throws Exception {

        Long id = 10L;

        mvc.perform(get("/api/users/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMvcCreateSuccess() throws Exception {

        List<String> roles = List.of("ROLE_READER");
        UserCUDTO userDTO = new UserCUDTO("Novi", "User", "password", "novi@gmail.com", roles);

        MvcResult result = mvc.perform(post("/api/users")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        User returnedUser = objectMapper.readValue(resultJson, User.class);

        assertEquals(userDTO.getName(), returnedUser.getName());
        assertEquals(userDTO.getLastName(), returnedUser.getLastName());
        assertEquals(userDTO.getEmail(), returnedUser.getEmail());

    }

    @Test
    public void testMvcCreateBadRequest() throws Exception {

        List<String> roles = List.of("ROLE_READER");
        UserCUDTO userDTO = new UserCUDTO("Novi", "User", "password", null, roles);

        mvc.perform(post("/api/users")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMvcUpdateSuccess() throws Exception {

        UserCUDTO userDTO = new UserCUDTO("Promenjeni", "User", "password", "promenjeni@gmail.com");
        Long userId = 2L;

        MvcResult result = mvc.perform(put("/api/users/{id}", userId)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        User returnedUser = objectMapper.readValue(resultJson, User.class);

        assertEquals(userDTO.getName(), returnedUser.getName());
        assertEquals(userDTO.getLastName(), returnedUser.getLastName());
        assertEquals(userDTO.getEmail(), returnedUser.getEmail());

    }

    @Test
    public void testMvcUpdateEntityNotFound() throws Exception {

        UserCUDTO userDTO = new UserCUDTO("Promenjeni", "User", "password", "promenjeni@gmail.com");
        Long id = 10L;

        mvc.perform(put("/api/users/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMvcDeleteSuccess() throws Exception {

        Long id = 3L;

        mvc.perform(delete("/api/users/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testMvcDeleteEntityNotFound() throws Exception {

        Long id = 10L;

        mvc.perform(delete("/api/users/{id}", id)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
