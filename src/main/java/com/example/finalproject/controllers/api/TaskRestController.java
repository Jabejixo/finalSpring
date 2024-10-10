package com.example.finalproject.controllers.api;

import com.example.finalproject.models.Task;
import com.example.finalproject.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskRestController {

    private final TaskService taskService;

    @Autowired
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Получение задачи по ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Получение всех задач
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    // Создание новой задачи
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // Обновление существующей задачи
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody Task updatedTask) {
        Task existingTask = taskService.findById(id);
        if (existingTask != null) {
            updatedTask.setId(id);
            taskService.update(updatedTask);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление задачи по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        Task existingTask = taskService.findById(id);
        if (existingTask != null) {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

