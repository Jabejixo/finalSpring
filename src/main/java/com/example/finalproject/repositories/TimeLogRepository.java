package com.example.finalproject.repositories;

import com.example.finalproject.models.TimeLog;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface TimeLogRepository extends BaseRepository<TimeLog>{

}
