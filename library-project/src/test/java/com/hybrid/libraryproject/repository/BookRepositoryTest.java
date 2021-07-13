package com.hybrid.libraryproject.repository;

import com.hybrid.libraryproject.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    public void TestFindByIsbnSuccess() {

        String isbn = "11111111111111";

        Book foundBook = repository.findByIsbn(isbn);

        assertThat(foundBook.getIsbn()).isEqualTo(isbn);
    }

    @Test
    public void TestFindByIsbnBookNotFound() {

        String isbn = "0000000000000";

        Book foundBook = repository.findByIsbn(isbn);

        assertThat(foundBook).isNull();
    }
}
