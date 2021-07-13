package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookRentalRDTO {

    private Long id;

    private LocalDate dateOfRenting;

    private LocalDate returnedDate;

    private UserRDTO user;

    private BookRDTO book;
}
