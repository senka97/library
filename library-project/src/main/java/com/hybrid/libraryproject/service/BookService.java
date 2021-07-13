package com.hybrid.libraryproject.service;

import com.hybrid.libraryproject.dto.BookCUDTO;
import com.hybrid.libraryproject.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BookService {

    Page<Book> getAll(Integer pageNumber);
    Page<Book> getAll(Map<String,String> queryParam, Integer pageNumber);
    Book create(BookCUDTO bookDTO);
    Book update(Long id, BookCUDTO bookDTO);
    void delete(Long id);
    Book findById(Long id);
    Book save(Book book);
    String uploadImage(MultipartFile file);
    List<Book> getMostRented(Integer pageNumber);
}
