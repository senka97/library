package com.hybrid.libraryproject.service;

import com.hybrid.libraryproject.dto.AuthorCUDTO;
import com.hybrid.libraryproject.model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> getAll();
    Author create(AuthorCUDTO authorDTO);
    Author update(Long id, AuthorCUDTO bookDTO);
    void delete(Long id);
    Author findById(Long id);
}
