package com.example.finalproject.repositories;

import com.example.finalproject.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface ProjectRepository extends BaseRepository<Project> {
    Page<Project> searchByName(String name, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"users", "tasks"})
    Page<Project> findAll(Pageable pageable);
}
