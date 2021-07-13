package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorCUDTO {

    @NotBlank(message = "Name can't be blank")
    private String name;
}
