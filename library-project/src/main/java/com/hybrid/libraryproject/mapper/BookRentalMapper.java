package com.hybrid.libraryproject.mapper;

import com.hybrid.libraryproject.dto.BookRentalCUDTO;
import com.hybrid.libraryproject.dto.BookRentalRDTO;
import com.hybrid.libraryproject.model.Book;
import com.hybrid.libraryproject.model.BookRental;
import com.hybrid.libraryproject.model.User;
import org.springframework.stereotype.Component;

@Component
public class BookRentalMapper {

    private final UserMapper userMapper;

    private final BookMapper bookMapper;

    public BookRentalMapper(UserMapper userMapper, BookMapper bookMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public BookRentalRDTO toDTO(BookRental bookRental){

        BookRentalRDTO bookRentalDTO = new BookRentalRDTO();

        bookRentalDTO.setId(bookRental.getId());
        bookRentalDTO.setDateOfRenting(bookRental.getDateOfRenting());
        bookRentalDTO.setReturnedDate(bookRental.getReturnedDate());

        bookRentalDTO.setUser(userMapper.toDTO(bookRental.getUser()));
        bookRentalDTO.setBook(bookMapper.toDTO(bookRental.getBook()));

        return bookRentalDTO;
    }

    public BookRental toEntity(BookRental bookRental, BookRentalCUDTO bookRentalDTO, User user, Book book){

        bookRental.setDateOfRenting(bookRentalDTO.getDateOfRenting());
        bookRental.setUser(user);
        bookRental.setBook(book);

        return  bookRental;
    }

}
