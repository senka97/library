package com.hybrid.libraryproject.repository;

import com.hybrid.libraryproject.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Book findByIsbn(String isbn);

    @Query(value = "select b from Book b order by b.rentingList.size desc")
    List<Book> getMostRented();
}
