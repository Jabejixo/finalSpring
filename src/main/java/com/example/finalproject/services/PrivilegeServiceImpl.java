package com.example.finalproject.services;

import com.example.finalproject.models.Privilege;
import com.example.finalproject.repositories.BaseRepository;
import com.example.finalproject.repositories.PrivilegeRepository;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeServiceImpl extends BaseServiceImpl<Privilege, PrivilegeRepository> implements PrivilegeService {
    public PrivilegeServiceImpl(PrivilegeRepository repository) {
        super(repository);
    }
}
