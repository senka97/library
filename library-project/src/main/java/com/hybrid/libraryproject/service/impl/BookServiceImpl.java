package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.dto.BookCUDTO;
import com.hybrid.libraryproject.exception.DeleteException;
import com.hybrid.libraryproject.mapper.BookMapper;
import com.hybrid.libraryproject.model.Author;
import com.hybrid.libraryproject.model.Book;
import com.hybrid.libraryproject.repository.BookRepository;
import com.hybrid.libraryproject.service.AuthorService;
import com.hybrid.libraryproject.service.BookService;
import com.hybrid.libraryproject.specification.BookSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    private final BookMapper mapper;

    private final AuthorService authorService;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, AuthorService authorService){
        this.repository = bookRepository;
        this.mapper = bookMapper;
        this.authorService = authorService;
    }

    @Value("${image-path}")
    private String IMAGE_PATH;

    @Override
    @Transactional(readOnly = true)
    public Page<Book> getAll(Integer pageNumber) {

        return repository.findAll(PageRequest.of(pageNumber, 5));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> getAll(Map<String,String> queryParams, Integer pageNumber) {

        return repository.findAll(new BookSpecification(queryParams), PageRequest.of(pageNumber, 5));
    }

    @Override
    @Transactional
    public Book create(BookCUDTO bookDTO) {

        List<Author> authors = new ArrayList<>();
        bookDTO.getAuthors().forEach(author -> authors.add(authorService.findById(author)));
        Book newBook = mapper.toEntity(new Book(), bookDTO, authors);
        return repository.save(newBook);
    }

    @Override
    @Transactional
    public Book update(Long id, BookCUDTO bookDTO) {

        List<Author> authors = new ArrayList<>();
        bookDTO.getAuthors().forEach(author -> authors.add(authorService.findById(author)));
        Book book = mapper.toEntity(findById(id), bookDTO, authors);
        return repository.save(book);
    }

    @Override
    @Transactional
    public void delete(Long id) throws DeleteException {

        Book book = findById(id);
        if (book.getRentedCopies() > 0) {
            throw new DeleteException(String.format("Book with id %d has rented copies and can't be deleted", id));
        }
        repository.deleteById(id);
    }

    public Book findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Book with id %d not found",id)));
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    public String uploadImage(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Path storageDirectory = Paths.get(IMAGE_PATH);

        if(!Files.exists(storageDirectory)){
            try {
                Files.createDirectories(storageDirectory);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Path destination = Paths.get(storageDirectory.toString() + "/" + fileName);

        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file

        } catch ( IOException e) {
            e.printStackTrace();
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("images/")
                .path(fileName)
                .toUriString();

        return fileDownloadUri;
    }

    @Override
    public List<Book> getMostRented(Integer size) {

        List<Book> books = repository.getMostRented();
        return books.stream().limit(size).collect(Collectors.toList());
    }

}
