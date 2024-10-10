package com.example.finalproject.controllers;

import com.example.finalproject.models.Project;
import com.example.finalproject.models.User;
import com.example.finalproject.services.ProjectService;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('VIEW_PROJECT')")
    @GetMapping
    public String getAllProjects(@RequestParam(defaultValue = "false") boolean deleted,
                                 @RequestParam(required = false) String search,
                                 Model model, @PageableDefault(size = 10) Pageable pageable,
                                 @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Page<Project> projects;
        if (search != null && !search.isEmpty()) {
            projects = projectService.searchByNameOrDescription(search, pageable);
        } else {
            projects = projectService.findByIsDeletedFalse(pageable);
        }

        if (htmxRequest != null) {
            model.addAttribute("projects", projects);
            return "fragments/projectTable :: projectTable";
        }

        model.addAttribute("projects", projects);
        model.addAttribute("search", search);
        return "projects";
    }

    @PreAuthorize("hasAuthority('CREATE_PROJECT')")
    @GetMapping("/add")
    public String addProjectForm(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        model.addAttribute("project", new Project());
        model.addAttribute("users", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/addProject :: addProject";
        }
        return "addProject";
    }

    @PreAuthorize("hasAuthority('CREATE_PROJECT')")
    @PostMapping
    public String saveProject(@ModelAttribute Project project) {
        projectService.save(project);
        return "redirect:/projects";
    }

    @PreAuthorize("hasAuthority('DELETE_PROJECT')")
    @PostMapping("/delete")
    public String deleteSelectedProjects(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> projectService.softDelete(projectService.findById(id)));
        model.addAttribute("projects", projectService.findByIsDeletedFalse(pageable));
        return "fragments/projectTable :: projectTable";
    }

    @PreAuthorize("hasAuthority('UPDATE_PROJECT')")
    @PostMapping("/restore")
    public String restoreSelectedProjects(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> projectService.restore(projectService.findById(id)));
        model.addAttribute("projects", projectService.findByIsDeletedFalse(pageable));
        return "fragments/projectTable :: projectTable";
    }

    @PreAuthorize("hasAuthority('UPDATE_PROJECT')")
    @GetMapping("/edit/{id}")
    public String editProjectForm(@PathVariable("id") UUID id, Model model,
                                  @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Project project = projectService.findById(id);
        model.addAttribute("project", project);
        model.addAttribute("users", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/editProject :: editProject";
        }
        return "editProject";
    }

    @PreAuthorize("hasAuthority('DELETE_PROJECT')")
    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable("id") UUID id, Model model, Pageable pageable) {
        projectService.delete(id);
        model.addAttribute("projects", projectService.findAll(pageable));
        return "fragments/projectTable :: projectTable";
    }

    @PreAuthorize("hasAuthority('UPDATE_PROJECT')")
    @PostMapping("/update")
    public String updateProject(@ModelAttribute Project project, Model model) {
        if (project.getId() == null) {
            model.addAttribute("error", "ID проекта не должен быть пустым");
            return "redirect:/projects";
        }
        projectService.update(project);
        return "redirect:/projects";
    }

    @PreAuthorize("hasAuthority('VIEW_PROJECT')")
    @GetMapping("/all")
    public String getAllProjectsIncludingDeleted(Model model, Pageable pageable) {
        model.addAttribute("projects", projectService.findAll(pageable));
        return "fragments/projectTable :: projectTable";
    }

    @PreAuthorize("hasAuthority('VIEW_PROJECT')")
    @GetMapping("/sorted")
    public String getSortedProjects(@RequestParam String sortField,
                                    @RequestParam(defaultValue = "asc") String sortDir,
                                    Model model, Pageable pageable) {
        Sort sort = sortDir.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Page<Project> sortedProjects = projectService.findAll(pageable, sort);
        model.addAttribute("projects", sortedProjects);
        return "fragments/projectTable :: projectTable";
    }
}
