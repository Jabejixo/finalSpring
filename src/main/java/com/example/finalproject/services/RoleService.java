package com.example.finalproject.services;

import com.example.finalproject.models.Role;

public interface RoleService extends BaseService<Role> {
    void createSuperUserRole();
}
