package com.example.finalproject.services;

import com.example.finalproject.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService extends BaseService<Task> {
    Page<Task> searchByTitleOrDescription(String search, Pageable pageable);
}
