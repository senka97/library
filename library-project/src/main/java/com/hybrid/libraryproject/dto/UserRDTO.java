package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRDTO {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private List<String> roles = new ArrayList<>();

}
