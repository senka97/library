package com.hybrid.libraryproject.mapper;

import com.hybrid.libraryproject.dto.UserCUDTO;
import com.hybrid.libraryproject.dto.UserRDTO;
import com.hybrid.libraryproject.model.Role;
import com.hybrid.libraryproject.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public UserRDTO toDTO(User user){

        UserRDTO userDTO = new UserRDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());

        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());

        user.getRoles().forEach(role -> userDTO.getRoles().add(role.getName()));

        return userDTO;
    }

    public User toEntity(User user, UserCUDTO userDTO, List<Role> roles){

        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        roles.forEach(role-> user.getRoles().add(role));

        return  user;
    }

    public User update(User user, UserCUDTO userDTO){

        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());

        return  user;
    }
}
