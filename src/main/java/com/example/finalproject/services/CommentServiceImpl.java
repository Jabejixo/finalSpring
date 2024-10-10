package com.example.finalproject.services;

import com.example.finalproject.models.Comment;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentServiceImpl extends BaseServiceImpl<Comment, CommentRepository> implements CommentService {
    public CommentServiceImpl(CommentRepository repository) {
        super(repository);
    }

    @Override
    public Page<Comment> findByTaskId(UUID taskId, Pageable pageable) {
        return repository.findByTaskId(taskId, pageable);
    }
}
