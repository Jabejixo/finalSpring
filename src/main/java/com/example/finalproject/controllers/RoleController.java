package com.example.finalproject.controllers;

import com.example.finalproject.models.Privilege;
import com.example.finalproject.models.Role;
import com.example.finalproject.repositories.RoleRepository;
import com.example.finalproject.services.PrivilegeService;
import com.example.finalproject.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    @Autowired
    public RoleController(RoleService roleService, PrivilegeService privilegeService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
    }

    // Получение всех ролей
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @GetMapping
    public String getAllRoles(Model model) {
        List<Role> roles = roleService.findByIsDeletedFalse();
        model.addAttribute("roles", roles);
        return "roles";
    }

    // Форма создания/редактирования роли
    @PreAuthorize("hasAuthority('MANAGE_ROLES') or hasAuthority('MANAGE_ROLES')")
    @GetMapping({"/new", "/edit/{id}"})
    public String showRoleForm(@PathVariable(required = false) UUID id, Model model) {
        Role role = (id == null) ? new Role() : roleService.findById(id);
        List<Privilege> allPrivileges = privilegeService.findAll(); // Получаем все привилегии
        model.addAttribute("role", role);
        model.addAttribute("allPrivileges", allPrivileges); // Добавляем привилегии в модель
        return "fragments/role-form :: role-form";
    }

    // Сохранение роли
    @PreAuthorize("hasAuthority('MANAGE_ROLES') or hasAuthority('MANAGE_ROLES')")
    @PostMapping({"/new", "/edit/{id}"})
    public String saveOrUpdateRole(@PathVariable(required = false) UUID id, @ModelAttribute("role") Role role) {
        if (id != null) {
            role.setId(id);
        }
        roleService.save(role);
        return "redirect:/roles";
    }

    // Логическое удаление роли
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PostMapping("/delete/{id}")
    public String softDelete(@PathVariable(required = false) UUID id) {
        roleService.delete(id);
        return "redirect:/roles";
    }

    // Массовое логическое удаление ролей
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PostMapping("/delete-multiple")
    public String softDeleteRoles(@RequestParam("ids") List<UUID> ids, Model model) {
        for (UUID id : ids) {
            roleService.softDelete(roleService.findById(id));
        }
        List<Role> roles = roleService.findByIsDeletedFalse();
        model.addAttribute("roles", roles);
        return "fragments/roleTable :: roleTable"; // Возвращаем только таблицу с ролями
    }

    // Массовое восстановление ролей
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PostMapping("/restore-multiple")
    public String restoreRoles(@RequestParam("ids") List<UUID> ids, Model model) {
        for (UUID id : ids) {
            roleService.restore(roleService.findById(id));
        }
        List<Role> roles = roleService.findByIsDeletedFalse();
        model.addAttribute("roles", roles);
        return "fragments/modelTable :: modelTable"; // Возвращаем только таблицу с ролями
    }

}

