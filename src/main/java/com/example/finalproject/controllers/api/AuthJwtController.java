package com.example.finalproject.controllers.api;

import com.example.finalproject.models.User;
import com.example.finalproject.models.dto.LoginDto;
import com.example.finalproject.models.dto.UserRegistrationDto;
import com.example.finalproject.models.requests.TokenRefreshRequest;
import com.example.finalproject.providers.JwtTokenProvider;
import com.example.finalproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/auth")
public class AuthJwtController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthJwtController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Асинхронная регистрация пользователя
    @Async
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<?>> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return CompletableFuture.supplyAsync(() -> {
            User newUser = userService.register(userRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        });
    }

    // Асинхронный вход пользователя
    @Async
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestBody LoginDto loginDto) {
        return CompletableFuture.supplyAsync(() -> {
            User user = userService.login(loginDto.getEmail(), loginDto.getPassword());
            return getResponseEntity(user);
        });
    }

    // Асинхронное обновление токена
    @Async
    @PostMapping("/refresh")
    public CompletableFuture<ResponseEntity<?>> refresh(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return CompletableFuture.supplyAsync(() -> {
            String refreshToken = tokenRefreshRequest.getRefreshToken();

            if (refreshToken == null || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            UUID userId = jwtTokenProvider.getUserId(refreshToken);
            User user = userService.findById(userId);

            return getResponseEntity(user);
        });
    }

    private ResponseEntity<?> getResponseEntity(User user) {
        String newAccessToken = jwtTokenProvider.generateToken(user.getId(), user.getRole().getId());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getRole().getId());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(tokens);
    }
}
