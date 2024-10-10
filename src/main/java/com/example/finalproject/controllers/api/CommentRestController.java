package com.example.finalproject.controllers.api;

import com.example.finalproject.models.Comment;
import com.example.finalproject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentRestController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Получение всех комментариев
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.findAll();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // Получение комментария по ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable UUID id) {
        Comment comment = commentService.findById(id);
        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Создание нового комментария
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.save(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // Обновление комментария
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable UUID id, @RequestBody Comment commentDetails) {
        Comment existingComment = commentService.findById(id);
        if (existingComment != null) {
            commentDetails.setId(id);
            Comment updatedComment = commentService.save(commentDetails);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Удаление комментария
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
