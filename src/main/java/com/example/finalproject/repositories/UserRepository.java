package com.example.finalproject.repositories;

import com.example.finalproject.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RepositoryRestResource(exported = false)
@Repository
public interface UserRepository extends BaseRepository<User> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Page<User> searchByEmailOrFirstName(String firstName, String lastName, Pageable pageable);
}
