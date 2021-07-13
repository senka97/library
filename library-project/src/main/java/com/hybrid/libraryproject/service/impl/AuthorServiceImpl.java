package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.dto.AuthorCUDTO;
import com.hybrid.libraryproject.exception.DeleteException;
import com.hybrid.libraryproject.mapper.AuthorMapper;
import com.hybrid.libraryproject.model.Author;
import com.hybrid.libraryproject.repository.AuthorRepository;
import com.hybrid.libraryproject.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    private final AuthorMapper mapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.repository = authorRepository;
        this.mapper = authorMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {

        return repository.findAll();
    }

    @Override
    @Transactional
    public Author create(AuthorCUDTO authorDTO) {

        Author newAuthor = mapper.toEntity(new Author(), authorDTO);
        return repository.save(newAuthor);
    }

    @Override
    @Transactional
    public Author update(Long id, AuthorCUDTO authorDTO) {

        Author author = mapper.toEntity(findById(id), authorDTO);
        return repository.save(author);
    }

    @Override
    @Transactional
    public void delete(Long id) throws DeleteException {

        Author author = findById(id);
        if(author.getBooks().size() != 0){
            throw new DeleteException(String.format("Author with id %d has books and can't be deleted", id));
        }
        repository.deleteById(id);
    }

    @Override
    public Author findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Author with id %d not found", id)));
    }
}
