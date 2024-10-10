package com.example.finalproject.services;

import com.example.finalproject.models.Tag;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends BaseServiceImpl<Tag, TagRepository> implements TagService {
    public TagServiceImpl(TagRepository repository) {
        super(repository);
    }
}
