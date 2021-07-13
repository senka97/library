package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookCUDTO {

    @NotBlank(message = "Title can't be blank")
    private String title;

    private String imageUrl;

    @NotBlank(message = "Isbn can't be blank")
    private String isbn;

    private LocalDate creationDate;

    @NotNull(message = "Number of copies in total is mandatory")
    @PositiveOrZero(message = "Number of copies in total must be positive number or zero")
    private Integer copiesTotal;

    @NotNull(message = "Number of rented copies is mandatory")
    @PositiveOrZero(message = "Number of rented copies must be positive number or zero")
    private Integer rentedCopies;

    private List<Long> authors = new ArrayList<>();
}
