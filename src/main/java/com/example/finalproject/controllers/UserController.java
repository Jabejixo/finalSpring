package com.example.finalproject.controllers;

import com.example.finalproject.models.User;
import com.example.finalproject.models.dto.UserRegistrationDto;
import com.example.finalproject.services.ProjectService;
import com.example.finalproject.services.RoleService;
import com.example.finalproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ProjectService projectService;

    @Autowired
    public UserController(UserService userService, RoleService roleService, ProjectService projectService) {
        this.userService = userService;
        this.roleService = roleService;
        this.projectService = projectService;
    }

    // Получение всех пользователей с пагинацией
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping
    public String getAllUsers(@RequestParam(defaultValue = "false") boolean deleted,
                              @RequestParam(required = false) String search,
                              Model model, @PageableDefault(size = 10) Pageable pageable,
                              @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Page<User> users;

        if (search != null && !search.isEmpty()) {
            users = userService.searchByEmailOrName(search, pageable);
        } else {
            users = userService.findAll(pageable, deleted);
        }

        if (htmxRequest != null) {
            model.addAttribute("users", users);
            return "fragments/userTable :: userTable";
        }

        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "users";
    }

    // Форма добавления пользователя
    @PreAuthorize("hasAuthority('CREATE_USER')")
    @GetMapping("/add")
    public String addUserForm(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("projects", projectService.findAll());

        if (htmxRequest != null) {
            return "fragments/addUser :: addUser";
        }

        return "addUser";
    }

    // Сохранение пользователя
    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping
    public String saveUser(@ModelAttribute User user) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail(user.getEmail());
        registrationDto.setPassword(user.getPassword());
        registrationDto.setFirstName(user.getFirstName());
        registrationDto.setLastName(user.getLastName());
        registrationDto.setRole(user.getRole());
        userService.register(registrationDto);
        return "redirect:/users";
    }

    // Удаление пользователей
    @PreAuthorize("hasAuthority('DELETE_USER')")
    @PostMapping("/delete")
    public String deleteSelectedUsers(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> userService.softDelete(userService.findById(id)));
        model.addAttribute("users", userService.findAll(pageable));
        return "fragments/userTable :: userTable";
    }

    // Восстановление пользователей
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/restore")
    public String restoreSelectedUsers(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> userService.restore(userService.findById(id)));
        model.addAttribute("users", userService.findAll(pageable));
        return "fragments/userTable :: userTable";
    }

    // Редактирование пользователя
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") UUID id, Model model,
                               @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("projects", projectService.findAll());

        if (htmxRequest != null) {
            return "fragments/editUser :: editUser";
        }

        return "editUser";
    }

    // Удаление пользователя по ID
    @PreAuthorize("hasAuthority('DELETE_USER')")
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") UUID id, Model model, Pageable pageable) {
        userService.delete(id);
        model.addAttribute("users", userService.findAll(pageable));
        return "fragments/userTable :: userTable";
    }

    // Обновление пользователя
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, Model model) {
        if (user.getId() == null) {
            model.addAttribute("error", "ID пользователя не должен быть пустым");
            return "redirect:/users";
        }
        userService.update(user);
        return "redirect:/users";
    }

    // Получение всех пользователей (включая удалённых)
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/all")
    public String getAllUsersIncludingDeleted(Model model, Pageable pageable) {
        model.addAttribute("users", userService.findAll(pageable));
        return "fragments/userTable :: userTable";
    }

    // Сортировка пользователей
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @GetMapping("/sorted")
    public String getSortedUsers(@RequestParam String sortField,
                                 @RequestParam(defaultValue = "asc") String sortDir,
                                 Model model, Pageable pageable) {
        Sort sort = sortDir.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Page<User> sortedUsers = userService.findAll(pageable, sort);
        model.addAttribute("users", sortedUsers);
        return "fragments/userTable :: userTable";
    }
}
