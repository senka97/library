package com.hybrid.libraryproject.mapper;

import com.hybrid.libraryproject.dto.AuthorCUDTO;
import com.hybrid.libraryproject.dto.AuthorRDTO;
import com.hybrid.libraryproject.model.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorRDTO toDTO(Author author){

        AuthorRDTO authorDTO = new AuthorRDTO();

        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());

        return authorDTO;
    }

    public Author toEntity(Author author, AuthorCUDTO authorDTO){

        author.setName(authorDTO.getName());

        return  author;
    }
}
