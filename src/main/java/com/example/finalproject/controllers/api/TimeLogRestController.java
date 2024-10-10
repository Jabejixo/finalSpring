package com.example.finalproject.controllers.api;

import com.example.finalproject.models.TimeLog;
import com.example.finalproject.services.TimeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timelogs")
public class TimeLogRestController {

    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogRestController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    // Получение лога времени по ID
    @GetMapping("/{id}")
    public ResponseEntity<TimeLog> getTimeLogById(@PathVariable UUID id) {
        TimeLog timeLog = timeLogService.findById(id);
        if (timeLog != null) {
            return ResponseEntity.ok(timeLog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Получение всех логов времени
    @GetMapping
    public ResponseEntity<List<TimeLog>> getAllTimeLogs() {
        List<TimeLog> timeLogs = timeLogService.findAll();
        return ResponseEntity.ok(timeLogs);
    }

    // Создание нового лога времени
    @PostMapping
    public ResponseEntity<TimeLog> createTimeLog(@RequestBody TimeLog timeLog) {
        TimeLog createdTimeLog = timeLogService.save(timeLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeLog);
    }

    // Обновление существующего лога времени
    @PutMapping("/{id}")
    public ResponseEntity<TimeLog> updateTimeLog(@PathVariable UUID id, @RequestBody TimeLog updatedTimeLog) {
        TimeLog existingTimeLog = timeLogService.findById(id);
        if (existingTimeLog != null) {
            updatedTimeLog.setId(id);
            timeLogService.update(updatedTimeLog);
            return ResponseEntity.ok(updatedTimeLog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление лога времени
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeLog(@PathVariable UUID id) {
        TimeLog existingTimeLog = timeLogService.findById(id);
        if (existingTimeLog != null) {
            timeLogService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
