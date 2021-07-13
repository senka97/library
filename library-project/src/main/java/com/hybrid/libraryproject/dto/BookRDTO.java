package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookRDTO {

    private Long id;

    private String title;

    private String imageUrl;

    private String isbn;

    private LocalDate creationDate;

    private Integer copiesTotal;

    private Integer rentedCopies;

    private List<String> authors = new ArrayList<>();

}
