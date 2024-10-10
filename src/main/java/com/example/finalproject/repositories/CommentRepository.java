package com.example.finalproject.repositories;

import com.example.finalproject.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@RepositoryRestResource(exported = false)
@Repository
public interface CommentRepository extends BaseRepository<Comment>{
    Page<Comment> findByTaskId(UUID taskId, Pageable pageable);
}
