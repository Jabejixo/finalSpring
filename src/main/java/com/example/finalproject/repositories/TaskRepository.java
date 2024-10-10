package com.example.finalproject.repositories;

import com.example.finalproject.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface TaskRepository extends BaseRepository<Task> {
    Page<Task> searchByTitle(String search, Pageable pageable);
}
