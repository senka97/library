package com.hybrid.libraryproject.service;

import com.hybrid.libraryproject.dto.UserCUDTO;
import com.hybrid.libraryproject.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User get(Long id);
    User create(UserCUDTO userDTO);
    User update(Long id, UserCUDTO userDTO);
    void delete(Long id);
    User findById(Long id);
}
