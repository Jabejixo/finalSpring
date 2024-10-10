package com.example.finalproject.controllers.api;


import com.example.finalproject.models.Role;
import com.example.finalproject.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleRestController {

    private final RoleService roleService;

    @Autowired
    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Получение всех ролей
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findByIsDeletedFalse();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Получение роли по ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable UUID id) {
        Role role = roleService.findById(id);
        if (role != null) {
            return new ResponseEntity<>(role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Создание новой роли
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.save(role);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    // Обновление роли
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable UUID id, @RequestBody Role roleDetails) {
        Role existingRole = roleService.findById(id);
        if (existingRole != null) {
            roleDetails.setId(id);
            Role updatedRole = roleService.save(roleDetails);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Удаление роли
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

