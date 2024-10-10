package com.example.finalproject.controllers.api;

import com.example.finalproject.models.Project;
import com.example.finalproject.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectRestController {

    private final ProjectService projectService;

    @Autowired
    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Получение всех проектов
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.findByIsDeletedFalse();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Получение проекта по ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        Project project = projectService.findById(id);
        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Создание нового проекта
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.save(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    // Обновление проекта
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody Project projectDetails) {
        Project existingProject = projectService.findById(id);
        if (existingProject != null) {
            projectDetails.setId(id);
            Project updatedProject = projectService.save(projectDetails);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Удаление проекта
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

