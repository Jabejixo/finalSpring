package com.example.finalproject.services;

import com.example.finalproject.models.Project;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project, ProjectRepository> implements ProjectService {
    public ProjectServiceImpl(ProjectRepository repository) {
        super(repository);
    }

    @Override
    public Page<Project> searchByNameOrDescription(String search, Pageable pageable) {
        return repository.searchByName(search, pageable);
    }

    @Override
    public Page<Project> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
