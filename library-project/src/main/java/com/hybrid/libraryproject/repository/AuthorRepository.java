package com.hybrid.libraryproject.repository;

import com.hybrid.libraryproject.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
