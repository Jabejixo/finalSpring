package com.example.finalproject.repositories;

import com.example.finalproject.models.Task_Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface Task_TagRepository extends BaseRepository<Task_Tag>{
}
