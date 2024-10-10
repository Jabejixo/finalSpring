package com.example.finalproject.services;

import com.example.finalproject.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface ProjectService extends BaseService<Project> {
    Page<Project> searchByNameOrDescription(String search, Pageable pageable);
}
