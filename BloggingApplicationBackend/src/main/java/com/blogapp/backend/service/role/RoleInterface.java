package com.blogapp.backend.service.role;

import com.blogapp.backend.model.Role;

public interface RoleInterface {
    String findRoleNameById(int id);
    int findRoleIdByName(String name);
    Role findRoleByName(String name);
}
