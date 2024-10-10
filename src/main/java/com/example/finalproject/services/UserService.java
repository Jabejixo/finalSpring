package com.example.finalproject.services;

import com.example.finalproject.models.User;
import com.example.finalproject.models.dto.UserRegistrationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends BaseService<User> {
    User register(UserRegistrationDto userRegistrationDto);
    User login(String email, String password);
    Page<User> searchByEmailOrName(String email, Pageable pageable);
}
