package com.example.finalproject.services;

import com.example.finalproject.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface CommentService extends BaseService<Comment> {

    Page<Comment> findByTaskId(UUID taskId, Pageable pageable);
}
