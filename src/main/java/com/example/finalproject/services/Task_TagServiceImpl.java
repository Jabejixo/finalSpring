package com.example.finalproject.services;

import com.example.finalproject.models.Task_Tag;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.Task_TagRepository;
import org.springframework.stereotype.Service;

@Service
public class Task_TagServiceImpl extends BaseServiceImpl<Task_Tag, Task_TagRepository> implements Task_TagService {
    public Task_TagServiceImpl(Task_TagRepository repository) {
        super(repository);
    }
}
