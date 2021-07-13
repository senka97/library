package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookRentalCUDTO {

    @NotNull(message = "Date of renting can't be blank")
    private LocalDate dateOfRenting;

    @NotNull(message = "Book can't be blank")
    private Long bookId;

    @NotNull(message = "User can't be blank")
    private Long userId;

}
