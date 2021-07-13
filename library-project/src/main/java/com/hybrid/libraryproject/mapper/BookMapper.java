package com.hybrid.libraryproject.mapper;

import com.hybrid.libraryproject.dto.BookCUDTO;
import com.hybrid.libraryproject.dto.BookRDTO;
import com.hybrid.libraryproject.model.Author;
import com.hybrid.libraryproject.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public BookRDTO toDTO(Book book){

        BookRDTO bookDTO = new BookRDTO();

        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setImageUrl(book.getImageUrl());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setCopiesTotal(book.getCopiesTotal());
        bookDTO.setRentedCopies(book.getRentedCopies());

        bookDTO.setCreationDate(book.getCreationDate());
        book.getAuthors().forEach(author -> bookDTO.getAuthors().add(author.getName()));

        return bookDTO;
    }

    public Book toEntity(Book book, BookCUDTO bookDTO, List<Author> authors){

        book.setTitle(bookDTO.getTitle());
        book.setImageUrl(bookDTO.getImageUrl());
        book.setIsbn(bookDTO.getIsbn());
        book.setCopiesTotal(bookDTO.getCopiesTotal());
        book.setRentedCopies(bookDTO.getRentedCopies());

        book.setCreationDate(bookDTO.getCreationDate());
        authors.forEach(author-> book.getAuthors().add(author));

        return  book;
    }

}
