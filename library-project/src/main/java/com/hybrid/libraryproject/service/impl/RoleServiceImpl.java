package com.hybrid.libraryproject.service.impl;

import com.hybrid.libraryproject.model.Role;
import com.hybrid.libraryproject.repository.RoleRepository;
import com.hybrid.libraryproject.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository roleRepository){
        this.repository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return repository.findByName(name);
    }
}
