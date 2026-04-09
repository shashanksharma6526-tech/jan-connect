package com.jan_connect.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    
    @NotBlank(message = "Refresh token must not be blank")
    private String refreshToken;
}