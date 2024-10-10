package com.example.finalproject.services;

import com.example.finalproject.models.Task;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends BaseServiceImpl<Task, TaskRepository> implements TaskService {
    public TaskServiceImpl(TaskRepository repository) {
        super(repository);
    }

    @Override
    public Page<Task> searchByTitleOrDescription(String search, Pageable pageable) {
        return repository.searchByTitle(search, pageable);
    }
}
