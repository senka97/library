package com.hybrid.libraryproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    private String imageUrl;

    @NotNull
    @Column(unique = true)
    private String isbn;

    private LocalDate creationDate;

    @NotNull
    private Integer rentedCopies;

    @NotNull
    private Integer copiesTotal;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookRental> rentingList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Author> authors = new HashSet<>();

    public Book(Long id, String title, String isbn, LocalDate creationDate, Integer rentedCopies, Integer copiesTotal) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.creationDate = creationDate;
        this.rentedCopies = rentedCopies;
        this.copiesTotal = copiesTotal;
    }
}
