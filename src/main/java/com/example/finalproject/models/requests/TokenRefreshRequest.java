package com.example.finalproject.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// DTO для обновления токена
@Data
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
