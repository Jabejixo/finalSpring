package com.example.finalproject.controllers;

import com.example.finalproject.models.Task;
import com.example.finalproject.models.TimeLog;
import com.example.finalproject.models.User;
import com.example.finalproject.services.TaskService;
import com.example.finalproject.services.TimeLogService;
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
@RequestMapping("/timelogs")
public class TimeLogController {

    private final TimeLogService timeLogService;
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService, TaskService taskService, UserService userService) {
        this.timeLogService = timeLogService;
        this.taskService = taskService;
        this.userService = userService;
    }

    // Получение всех логов времени
    @PreAuthorize("hasAuthority('VIEW_TIME_LOGS')")
    @GetMapping
    public String getAllTimeLogs(@RequestParam(defaultValue = "false") boolean deleted, Model model,
                                 @PageableDefault(size = 10) Pageable pageable,
                                 @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Page<TimeLog> timeLogs = timeLogService.findByIsDeletedFalse(pageable);

        if (htmxRequest != null) {
            model.addAttribute("timeLogs", timeLogs);
            return "fragments/timelogTable :: timelogTable";
        }

        model.addAttribute("timeLogs", timeLogs);
        return "timelogs";
    }

    // Форма добавления лога времени
    @PreAuthorize("hasAuthority('LOG_TIME')")
    @GetMapping("/add")
    public String addTimeLogForm(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        model.addAttribute("timeLog", new TimeLog());
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/addTimeLog :: addTimeLog";
        }
        return "addTimeLog";
    }

    // Сохранение лога времени
    @PreAuthorize("hasAuthority('LOG_TIME')")
    @PostMapping
    public String saveTimeLog(@ModelAttribute TimeLog timeLog) {
        timeLogService.save(timeLog);
        return "redirect:/timelogs";
    }

    // Удаление логов времени
    @PreAuthorize("hasAuthority('DELETE_TIME_LOG')")
    @PostMapping("/delete")
    public String deleteSelectedTimeLogs(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> timeLogService.softDelete(timeLogService.findById(id)));
        model.addAttribute("timeLogs", timeLogService.findByIsDeletedFalse(pageable));
        return "fragments/timelogTable :: timelogTable";
    }

    // Восстановление логов времени
    @PreAuthorize("hasAuthority('UPDATE_TIME_LOG')")
    @PostMapping("/restore")
    public String restoreSelectedTimeLogs(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> timeLogService.restore(timeLogService.findById(id)));
        model.addAttribute("timeLogs", timeLogService.findByIsDeletedFalse(pageable));
        return "fragments/timelogTable :: timelogTable";
    }

    // Редактирование лога времени
    @PreAuthorize("hasAuthority('UPDATE_TIME_LOG')")
    @GetMapping("/edit/{id}")
    public String editTimeLogForm(@PathVariable("id") UUID id, Model model,
                                  @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        TimeLog timeLog = timeLogService.findById(id);
        model.addAttribute("timeLog", timeLog);
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/editTimeLog :: editTimeLog";
        }
        return "editTimeLog";
    }

    // Обновление лога времени
    @PreAuthorize("hasAuthority('UPDATE_TIME_LOG')")
    @PostMapping("/update")
    public String updateTimeLog(@ModelAttribute TimeLog timeLog) {
        timeLogService.update(timeLog);
        return "redirect:/timelogs";
    }

    // Получение всех логов времени (включая удалённые)
    @PreAuthorize("hasAuthority('VIEW_TIME_LOGS')")
    @GetMapping("/all")
    public String getAllTimeLogsIncludingDeleted(Model model, Pageable pageable) {
        model.addAttribute("timeLogs", timeLogService.findAll(pageable));
        return "fragments/timelogTable :: timelogTable";
    }

}
