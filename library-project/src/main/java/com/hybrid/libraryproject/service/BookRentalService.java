package com.hybrid.libraryproject.service;

import com.hybrid.libraryproject.dto.BookRentalCUDTO;
import com.hybrid.libraryproject.model.BookRental;

import java.util.List;

public interface BookRentalService {

    List<BookRental> getAll();
    BookRental get(Long id);
    BookRental create(BookRentalCUDTO bookRentalCUDTO);
    BookRental update(Long id);
    void delete(Long id);
    BookRental findById(Long id);
    List<BookRental> getAllOverdueBookReturns();

}
