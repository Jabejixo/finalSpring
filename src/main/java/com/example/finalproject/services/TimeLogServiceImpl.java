package com.example.finalproject.services;

import com.example.finalproject.models.TimeLog;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.TimeLogRepository;
import org.springframework.stereotype.Service;

@Service
public class TimeLogServiceImpl extends BaseServiceImpl<TimeLog, TimeLogRepository> implements TimeLogService {
    public TimeLogServiceImpl(TimeLogRepository repository) {
        super(repository);
    }
}
