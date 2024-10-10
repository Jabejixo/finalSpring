package com.example.finalproject.controllers.api;

import com.example.finalproject.models.Tag;
import com.example.finalproject.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
public class TagRestController {

    private final TagService tagService;

    @Autowired
    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    // Получение тега по ID
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable UUID id) {
        Tag tag = tagService.findById(id);
        if (tag != null) {
            return ResponseEntity.ok(tag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Получение всех тегов
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    // Создание нового тега
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagService.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    // Обновление существующего тега
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable UUID id, @RequestBody Tag updatedTag) {
        Tag existingTag = tagService.findById(id);
        if (existingTag != null) {
            updatedTag.setId(id);
            tagService.update(updatedTag);
            return ResponseEntity.ok(updatedTag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление тега по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        Tag existingTag = tagService.findById(id);
        if (existingTag != null) {
            tagService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

