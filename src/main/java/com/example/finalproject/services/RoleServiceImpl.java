package com.example.finalproject.services;

import com.example.finalproject.models.Privilege;
import com.example.finalproject.models.Role;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.PrivilegeRepository;
import com.example.finalproject.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleRepository> implements RoleService {
    private final PrivilegeRepository privilegeRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        super(roleRepository); // Здесь используется RoleRepository
        this.privilegeRepository = privilegeRepository;
    }

    @PostConstruct
    public void init() {
        if (!repository.existsByName("SUPERUSER")) createSuperUserRole();
    }

    @Transactional
    @Override
    public void createSuperUserRole() {
        Role role = new Role();
        role.setName("SUPERUSER");
        Set<Privilege> privileges = new HashSet<>(privilegeRepository.findAll());
        role.setPrivileges(privileges);
        repository.save(role);
    }
}
