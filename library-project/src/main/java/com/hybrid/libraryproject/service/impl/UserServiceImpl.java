package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.dto.UserCUDTO;
import com.hybrid.libraryproject.mapper.UserMapper;
import com.hybrid.libraryproject.model.Role;
import com.hybrid.libraryproject.model.User;
import com.hybrid.libraryproject.repository.UserRepository;
import com.hybrid.libraryproject.service.RoleService;
import com.hybrid.libraryproject.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService){
        this.repository = userRepository;
        this.mapper = userMapper;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {

        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User get(Long id) {

        return findById(id);
    }

    @Override
    @Transactional
    public User create(UserCUDTO userDTO) {

        List<Role> roles = new ArrayList<>();
        userDTO.getRoles().forEach(roleName -> roles.add(roleService.findByName(roleName)));
        User newUser = mapper.toEntity(new User(), userDTO, roles);
        return repository.save(newUser);
    }

    @Override
    @Transactional
    public User update(Long id, UserCUDTO userDTO) {

        User user = mapper.update(findById(id), userDTO);
        return repository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        repository.deleteById(id);
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id %d not found",id)));
    }
}
