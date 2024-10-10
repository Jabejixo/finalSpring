package com.example.finalproject.repositories;

import com.example.finalproject.models.Privilege;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@RepositoryRestResource(exported = false)
@Repository
public interface PrivilegeRepository extends BaseRepository<Privilege>{

}
