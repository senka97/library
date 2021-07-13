package com.hybrid.libraryproject.service;

import com.hybrid.libraryproject.model.Role;

public interface RoleService {

    Role findByName(String name);
}
