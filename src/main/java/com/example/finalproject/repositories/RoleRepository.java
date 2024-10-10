package com.example.finalproject.repositories;

import com.example.finalproject.models.Role;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface RoleRepository extends BaseRepository<Role> {
    boolean existsByName(String name);

    Role findByName(String name);
}
