package com.example.finalproject.controllers;

import com.example.finalproject.models.Project;
import com.example.finalproject.models.Task;
import com.example.finalproject.models.User;
import com.example.finalproject.services.ProjectService;
import com.example.finalproject.services.TaskService;
import com.example.finalproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
    }

    // Получение всех задач
    @PreAuthorize("hasAuthority('VIEW_TASK')")
    @GetMapping
    public String getAllTasks(@RequestParam(defaultValue = "false") boolean deleted,
                              @RequestParam(required = false) String search,
                              Model model, @PageableDefault(size = 10) Pageable pageable,
                              @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Page<Task> tasks;
        if (search != null && !search.isEmpty()) {
            tasks = taskService.searchByTitleOrDescription(search, pageable);
        } else {
            tasks = taskService.findAll(pageable, deleted);
        }

        if (htmxRequest != null) {
            model.addAttribute("tasks", tasks);
            return "fragments/taskTable :: taskTable";
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("search", search);
        return "tasks";
    }

    // Форма добавления задачи
    @PreAuthorize("hasAuthority('CREATE_TASK')")
    @GetMapping("/add")
    public String addTaskForm(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        model.addAttribute("task", new Task());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("assignees", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/addTask :: addTask";
        }
        return "addTask";
    }

    // Сохранение задачи
    @PreAuthorize("hasAuthority('CREATE_TASK')")
    @PostMapping("/add")
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult result) {
        if (result.hasErrors()) {
            // Обработка ошибок
            return "form"; // или ваша страница с формой
        }
        taskService.save(task);
        return "redirect:/tasks";
    }

    // Удаление выбранных задач
    @PreAuthorize("hasAuthority('DELETE_TASK')")
    @PostMapping("/delete")
    public String deleteSelectedTasks(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> taskService.softDelete(taskService.findById(id)));
        model.addAttribute("tasks", taskService.findAll(pageable));
        return "fragments/taskTable :: taskTable";
    }

    // Восстановление выбранных задач
    @PreAuthorize("hasAuthority('UPDATE_TASK')")
    @PostMapping("/restore")
    public String restoreSelectedTasks(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> taskService.restore(taskService.findById(id)));
        model.addAttribute("tasks", taskService.findAll(pageable));
        return "fragments/taskTable :: taskTable";
    }

    // Форма редактирования задачи
    @PreAuthorize("hasAuthority('UPDATE_TASK')")
    @GetMapping("/edit/{id}")
    public String editTaskForm(@PathVariable("id") UUID id, Model model,
                               @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Task task = taskService.findById(id);
        model.addAttribute("task", task);
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("assignees", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/editTask :: editTask";
        }
        return "editTask";
    }

    // Удаление задачи по ID
    @PreAuthorize("hasAuthority('DELETE_TASK')")
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") UUID id, Model model, Pageable pageable) {
        taskService.delete(id);
        model.addAttribute("tasks", taskService.findAll(pageable));
        return "fragments/taskTable :: taskTable";
    }

    // Обновление задачи
    @PreAuthorize("hasAuthority('UPDATE_TASK')")
    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task, Model model) {
        if (task.getId() == null) {
            model.addAttribute("error", "ID задачи не должен быть пустым");
            return "redirect:/tasks";
        }
        taskService.update(task);
        return "redirect:/tasks";
    }

    // Получение всех задач (включая удалённые)
    @PreAuthorize("hasAuthority('VIEW_TASK')")
    @GetMapping("/all")
    public String getAllTasksIncludingDeleted(Model model, Pageable pageable) {
        model.addAttribute("tasks", taskService.findAll(pageable));
        return "fragments/taskTable :: taskTable";
    }

    // Сортировка задач
    @PreAuthorize("hasAuthority('VIEW_TASK')")
    @GetMapping("/sorted")
    public String getSortedTasks(@RequestParam String sortField,
                                 @RequestParam(defaultValue = "asc") String sortDir,
                                 Model model, Pageable pageable) {
        Sort sort = sortDir.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Page<Task> sortedTasks = taskService.findAll(pageable, sort);
        model.addAttribute("tasks", sortedTasks);
        return "fragments/taskTable :: taskTable";
    }
}
