package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.dto.BookRentalCUDTO;
import com.hybrid.libraryproject.exception.BusinessException;
import com.hybrid.libraryproject.mapper.BookRentalMapper;
import com.hybrid.libraryproject.model.Book;
import com.hybrid.libraryproject.model.BookRental;
import com.hybrid.libraryproject.model.User;
import com.hybrid.libraryproject.repository.BookRentalRepository;
import com.hybrid.libraryproject.service.BookRentalService;
import com.hybrid.libraryproject.service.BookService;
import com.hybrid.libraryproject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookRentalServiceImpl implements BookRentalService {

    private final BookRentalRepository repository;

    private final BookRentalMapper mapper;

    private final BookService bookService;

    private final UserService userService;

    @Value("${rental-period}")
    private Long RENTAL_PERIOD;

    public BookRentalServiceImpl(BookRentalRepository bookRentalRepository, BookRentalMapper bookRentalMapper,
                                 BookService bookService, UserService userService) {
        this.repository = bookRentalRepository;
        this.mapper = bookRentalMapper;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookRental> getAll() {

        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public BookRental get(Long id) {

        return findById(id);
    }

    @Override
    @Transactional
    public BookRental create(BookRentalCUDTO bookRentalDTO){

        rentBook(bookService.findById(bookRentalDTO.getBookId()));
        User user = userService.findById(bookRentalDTO.getUserId());
        Book book = bookService.findById(bookRentalDTO.getBookId());
        BookRental newBookRental = mapper.toEntity(new BookRental(), bookRentalDTO, user, book);
        return repository.save(newBookRental);
    }

    @Override
    @Transactional
    public BookRental update(Long id) {

        BookRental bookRental = findById(id);
        returnBook(bookRental);
        bookRental.setReturnedDate(LocalDate.now());
        return repository.save(bookRental);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        repository.deleteById(id);
    }

    @Override
    public BookRental findById(Long id) {

        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Book rental with id %d not found", id)));
    }

    @Override
    public List<BookRental> getAllOverdueBookReturns() {

        return repository.getAllOverdueBookReturns(LocalDate.now().minusDays(RENTAL_PERIOD));
    }

    private void returnBook(BookRental bookRental) throws BusinessException{

        if(bookRental.getReturnedDate() != null){
            throw new BusinessException(String.format("Book rental with id %d already returned", bookRental.getId()));
        }
        Book book = bookRental.getBook();
        book.setRentedCopies(book.getRentedCopies() - 1);
        bookService.save(book);
    }

    private void rentBook(Book book) throws BusinessException {

        if(book.getCopiesTotal() - book.getRentedCopies() == 0){
            throw new BusinessException(String.format("Book with id %d doesn't have available copies", book.getId()));
        }
        book.setRentedCopies(book.getRentedCopies() + 1);
        bookService.save(book);
    }
}
