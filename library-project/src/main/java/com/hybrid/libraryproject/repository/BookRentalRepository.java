package com.hybrid.libraryproject.repository;

import com.hybrid.libraryproject.model.BookRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookRentalRepository extends JpaRepository<BookRental, Long> {

    @Query(value = "select br from BookRental br where br.dateOfRenting < ?1 and br.returnedDate is null")
    List<BookRental> getAllOverdueBookReturns(LocalDate date);
}
