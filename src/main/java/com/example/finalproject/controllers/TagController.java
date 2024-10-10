package com.example.finalproject.controllers;

import com.example.finalproject.models.Tag;
import com.example.finalproject.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // Получение всех тегов
    @PreAuthorize("hasAuthority('VIEW_TAG')")
    @GetMapping
    public String getAllTags(@RequestParam(defaultValue = "false") boolean deleted,
                             Model model, @PageableDefault(size = 10) Pageable pageable,
                             @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Page<Tag> tags = tagService.findAll(pageable, deleted);

        if (htmxRequest != null) {
            model.addAttribute("tags", tags);
            return "fragments/tagTable :: tagTable";
        }

        model.addAttribute("tags", tags);
        return "tags";
    }

    // Форма добавления тега
    @PreAuthorize("hasAuthority('CREATE_TAG')")
    @GetMapping("/add")
    public String addTagForm(Model model, @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        model.addAttribute("tag", new Tag());

        if (htmxRequest != null) {
            return "fragments/addTag :: addTag";
        }

        return "addTag";
    }

    // Сохранение тега
    @PreAuthorize("hasAuthority('CREATE_TAG')")
    @PostMapping
    public String saveTag(@ModelAttribute Tag tag) {
        tagService.save(tag);
        return "redirect:/tags";
    }

    // Удаление выбранных тегов
    @PreAuthorize("hasAuthority('DELETE_TAG')")
    @PostMapping("/delete")
    public String deleteSelectedTags(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> tagService.softDelete(tagService.findById(id)));
        model.addAttribute("tags", tagService.findAll(pageable));
        return "fragments/tagTable :: tagTable";
    }

    // Восстановление выбранных тегов
    @PreAuthorize("hasAuthority('UPDATE_TAG')")
    @PostMapping("/restore")
    public String restoreSelectedTags(@RequestParam List<UUID> ids, Model model, Pageable pageable) {
        ids.forEach(id -> tagService.restore(tagService.findById(id)));
        model.addAttribute("tags", tagService.findAll(pageable));
        return "fragments/tagTable :: tagTable";
    }

    // Форма редактирования тега
    @PreAuthorize("hasAuthority('UPDATE_TAG')")
    @GetMapping("/edit/{id}")
    public String editTagForm(@PathVariable("id") UUID id, Model model,
                              @RequestHeader(value = "HX-Request", required = false) String htmxRequest) {
        Tag tag = tagService.findById(id);
        model.addAttribute("tag", tag);

        if (htmxRequest != null) {
            return "fragments/editTag :: editTag";
        }

        return "editTag";
    }

    // Обновление тега
    @PreAuthorize("hasAuthority('UPDATE_TAG')")
    @PostMapping("/update")
    public String updateTag(@ModelAttribute Tag tag) {
        if (tag.getId() == null) {
            //Обработка ошибки -  ID тега не может быть пустым
            return "redirect:/tags";
        }
        tagService.update(tag);
        return "redirect:/tags";
    }

    // Получение всех тегов (включая удалённые)
    @PreAuthorize("hasAuthority('VIEW_TAG')")
    @GetMapping("/all")
    public String getAllTagsIncludingDeleted(Model model, Pageable pageable) {
        model.addAttribute("tags", tagService.findAll(pageable));
        return "fragments/tagTable :: tagTable";
    }

}

