package com.example.finalproject.controllers;

import com.example.finalproject.models.Comment;
import com.example.finalproject.models.Task;
import com.example.finalproject.models.User;
import com.example.finalproject.services.CommentService;
import com.example.finalproject.services.TaskService;
import com.example.finalproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, TaskService taskService, UserService userService) {
        this.commentService = commentService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('VIEW_COMMENT')")
    @GetMapping
    public String getAllComments(@RequestParam(defaultValue = "false") boolean deleted, Model model,
                                 @PageableDefault(size = 10) Pageable pageable,
                                 @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Page<Comment> comments = commentService.findAll(pageable, deleted);

        if (htmxRequest != null) {
            model.addAttribute("comments", comments);
            return "fragments/commentTable :: commentTable";
        }

        model.addAttribute("comments", comments);
        return "comments";
    }

    @PreAuthorize("hasAuthority('CREATE_COMMENT')")
    @GetMapping("/add")
    public String addCommentForm(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/addComment :: addComment";
        }
        return "addComment";
    }

    @PreAuthorize("hasAuthority('CREATE_COMMENT')")
    @PostMapping
    public String saveComment(@ModelAttribute Comment comment) {
        commentService.save(comment);
        return "redirect:/comments";
    }

    @PreAuthorize("hasAuthority('DELETE_COMMENT')")
    @PostMapping("/delete")
    public String deleteSelectedComments(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> commentService.softDelete(commentService.findById(id)));
        model.addAttribute("comments", commentService.findAll(pageable));
        return "fragments/commentTable :: commentTable";
    }

    @PreAuthorize("hasAuthority('UPDATE_COMMENT')")
    @PostMapping("/restore")
    public String restoreSelectedComments(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> commentService.restore(commentService.findById(id)));
        model.addAttribute("comments", commentService.findAll(pageable));
        return "fragments/commentTable :: commentTable";
    }

    @PreAuthorize("hasAuthority('UPDATE_COMMENT')")
    @GetMapping("/edit/{id}")
    public String editCommentForm(@PathVariable("id") UUID id, Model model,
                                  @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Comment comment = commentService.findById(id);
        model.addAttribute("comment", comment);
        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());

        if (htmxRequest != null) {
            return "fragments/editComment :: editComment";
        }
        return "editComment";
    }

    @PreAuthorize("hasAuthority('UPDATE_COMMENT')")
    @PostMapping("/update")
    public String updateComment(@ModelAttribute Comment comment) {
        commentService.update(comment);
        return "redirect:/comments";
    }

    @PreAuthorize("hasAuthority('DELETE_COMMENT')")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") UUID id, Model model, Pageable pageable) {
        commentService.delete(id);
        model.addAttribute("comments", commentService.findAll(pageable));
        return "fragments/commentTable :: commentTable";
    }

    @PreAuthorize("hasAuthority('VIEW_COMMENT')")
    @GetMapping("/all")
    public String getAllCommentsIncludingDeleted(Model model, Pageable pageable) {
        model.addAttribute("comments", commentService.findAll(pageable));
        return "fragments/commentTable :: commentTable";
    }
}
