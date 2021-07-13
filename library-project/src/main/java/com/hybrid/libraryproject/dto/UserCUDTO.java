package com.hybrid.libraryproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCUDTO {

    private String name;

    private String lastName;

    private String password;

    @NotNull(message = "Email is mandatory")
    private String email;

    private List<String> roles = new ArrayList<>();

    public UserCUDTO(String name, String lastName, String password, String email) {
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }
}
