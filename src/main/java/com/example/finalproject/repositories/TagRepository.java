package com.example.finalproject.repositories;

import com.example.finalproject.models.Tag;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface TagRepository extends BaseRepository<Tag>{

}
