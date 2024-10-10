package com.example.finalproject.services;

import com.example.finalproject.models.User;
import com.example.finalproject.models.dto.UserRegistrationDto;
import com.example.finalproject.repositories.RoleRepository;
import com.example.finalproject.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserRepository> implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        super(userRepository);
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        if (!repository.existsByEmail("Ivanzv14007@gmail.com")) {
            User user = new User();
            user.setPassword(passwordEncoder.encode("31014891"));
            user.setFirstName("Ivan");
            user.setLastName("Zviagintsev");
            user.setEmail("Ivanzv14007@gmail.com");
            user.setRole(roleRepository.findByName("SUPERUSER"));
            repository.save(user);
        }
    }

    @Override
    @Transactional
    public User register(UserRegistrationDto userRegistrationDto) {
        if (repository.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setRole(userRegistrationDto.getRole());
        return save(user);
    }

    @Override
    public User login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        return user;
    }

    @Override
    public Page<User> searchByEmailOrName(String email, Pageable pageable) {
        return repository.searchByEmailOrFirstName(email, "", pageable);
    }

    @Override
    public Page<User> findAll(Pageable pageable, boolean isDeleted) {
        if (isDeleted) {
            return repository.findAll(pageable);
        } else return repository.findAllByIsDeletedFalse(pageable);
    }
}
