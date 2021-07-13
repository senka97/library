package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.dto.BookCUDTO;
import com.hybrid.libraryproject.mapper.BookMapper;
import com.hybrid.libraryproject.model.Author;
import com.hybrid.libraryproject.model.Book;
import com.hybrid.libraryproject.repository.BookRepository;
import com.hybrid.libraryproject.service.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper mapper;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookServiceImpl service;

    private static Long book1Id = 1L;
    private static Long book2Id = 2L;
    private static Long book3Id = 3L;

    @Test
    public void testGetAllSuccess() {

        Book book1 = new Book(book1Id, "Title 1", "1234", LocalDate.now(), 10, 0);
        Book book2 = new Book(book2Id, "Title 2", "5678", LocalDate.now(), 10, 0);
        Page<Book> books = new PageImpl<>(List.of(book1,book2));

        when(repository.findAll(PageRequest.of(0,5))).thenReturn(books);
        Page<Book> foundBooks = service.getAll(0);

        verify(repository, times(1)).findAll(PageRequest.of(0,5));
        Assertions.assertEquals(books, foundBooks);
    }

    /*@Test
    public void testGetSuccess() {

        Book book1 = new Book(book1Id, "Title 1", "1234", LocalDate.now(), 10, 0);

        when(repository.findById(book1Id)).thenReturn(Optional.of(book1));
        Book foundBook = service.get(book1Id);

        verify(repository, times(1)).findById(book1Id);
        Assertions.assertEquals(book1.getId(), foundBook.getId());
    }

    @Test
    public void testGetEntityNotFoundException() {

        assertThrows(EntityNotFoundException.class, () -> service.get(book1Id));
    }*/

    @Test
    public void testCreateSuccess() {

        List<Long> authorIds = List.of(2L);
        Author author1 = new Author("Author");
        List<Author> authors = List.of(author1);
        Book book1 = new Book(book1Id, "Title 1", "1234", LocalDate.now(), 10, 0);
        BookCUDTO bookCUDTO = new BookCUDTO("Title 1", "", "1234", LocalDate.now(), 10, 0, authorIds);

        when(authorService.findById(2L)).thenReturn(author1);
        when(mapper.toEntity(new Book(), bookCUDTO, authors)).thenReturn(book1);
        when(repository.save(book1)).thenReturn(book1);

        Book createdBook = service.create(bookCUDTO);
        Assertions.assertEquals(book1, createdBook);
    }

    @Test
    public void testUpdateSuccess() {

        List<Long> authorIds = List.of(2L);
        Book book1 = new Book(book1Id, "Title 1", "1234", LocalDate.now(), 10, 0);
        BookCUDTO bookCUDTO = new BookCUDTO("Title 1", "", "1234", LocalDate.now(), 10, 0, authorIds);
        Book book1Changed = new Book(book1Id, "Title 1 Changed", "1234", LocalDate.now(), 10, 0);
        Author author1 = new Author("Author");
        List<Author> authors = List.of(author1);

        when(authorService.findById(2L)).thenReturn(author1);
        when(repository.findById(book1Id)).thenReturn(Optional.of(book1));
        when(mapper.toEntity(book1, bookCUDTO, authors)).thenReturn(book1Changed);
        when(repository.save(book1Changed)).thenReturn(book1Changed);

        Book updatedBook = service.update(book1Id, bookCUDTO);
        verify(repository, times(1)).save(book1Changed);
        Assertions.assertEquals(book1Changed, updatedBook);
    }

    @Test
    public void testUpdateEntityNotFoundException() {

        List<Long> authors = List.of(2L);
        BookCUDTO bookCUDTO = new BookCUDTO("Title 1", "", "1234", LocalDate.now(), 10, 0, authors);
        assertThrows(EntityNotFoundException.class, () -> service.update(book1Id, bookCUDTO));
    }

    @Test
    public void testDeleteSuccess() {

        Book book1 = new Book(book1Id, "Title 1", "1234", LocalDate.now(), 0, 10);

        when(repository.findById(book3Id)).thenReturn(Optional.of(book1));

        service.delete(book3Id);
        verify(repository, times(1)).deleteById(book3Id);
    }

}
