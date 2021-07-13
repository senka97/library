package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.dto.UserCUDTO;
import com.hybrid.libraryproject.mapper.UserMapper;
import com.hybrid.libraryproject.model.Role;
import com.hybrid.libraryproject.model.User;
import com.hybrid.libraryproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserMapper mapper;

    private static Long user1Id = 1L;
    private static Long user2Id = 2L;

    @Test
    public void testGetAllSuccess() {

        User user1 = new User(user1Id, "User1", "User1", "user1@gmail.com", "123");
        User user2 = new User(user2Id, "User2", "User2", "user2@gmail.com", "456");
        List<User> users = List.of(user1, user2);

        when(repository.findAll()).thenReturn(users);
        List<User> foundUsers = service.getAll();

        verify(repository, times(1)).findAll();
        Assertions.assertEquals(users, foundUsers);
    }

    @Test
    public void testGetSuccess() {

        User user1 = new User(user1Id, "User1", "User1", "user1@gmail.com", "123");

        when(repository.findById(user1Id)).thenReturn(Optional.of(user1));
        User foundUser = service.get(user1Id);

        verify(repository, times(1)).findById(user1Id);
        Assertions.assertEquals(user1, foundUser);
    }

    @Test
    public void testGetEntityNotFoundException() {

        assertThrows(EntityNotFoundException.class, () -> service.get(user1Id));
    }

    @Test
    public void testCreateSuccess() {

        User user1 = new User(user1Id, "Novi", "User", "novi@gmail.com", "password");
        UserCUDTO userCUDTO = new UserCUDTO("Novi", "User", "password", "novi@gmail.com");
        List<Role> roles = new ArrayList<>();

        when(mapper.toEntity(new User(), userCUDTO, roles)).thenReturn(user1);
        when(repository.save(user1)).thenReturn(user1);

        User createdUser = service.create(userCUDTO);
        Assertions.assertEquals(user1, createdUser);
    }

    @Test
    public void testUpdateSuccess() {

        User user1 = new User(user1Id, "Pera", "Peric", "pera@gmail.com", "password");
        UserCUDTO userCUDTO = new UserCUDTO("Pera Changed", "Peric", "password", "novi@gmail.com");
        User user1Changed = new User(user1Id, "Pera Changed", "Peric", "novi@gmail.com", "password");

        when(repository.findById(user1Id)).thenReturn(Optional.of(user1));
        when(mapper.update(user1, userCUDTO)).thenReturn(user1Changed);
        when(repository.save(user1Changed)).thenReturn(user1Changed);

        User updatedUser = service.update(user1Id, userCUDTO);
        verify(repository, times(1)).save(user1Changed);
        Assertions.assertEquals(user1Changed, updatedUser);
    }

    @Test
    public void testUpdateEntityNotFoundException() {

        UserCUDTO userCUDTO = new UserCUDTO("Pera Changed", "Peric", "password", "novi@gmail.com");
        assertThrows(EntityNotFoundException.class, () -> service.update(user1Id, userCUDTO));
    }

    @Test
    public void testDeleteSuccess() {

        service.delete(user1Id);
        verify(repository, times(1)).deleteById(user1Id);
    }
}
