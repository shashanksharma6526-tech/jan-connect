package com.jan_connect.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    
    private String acessToken;
    private String refreshToken;

    private Long userId;
    private String username;
    private String email;
    private String userType;
    private String avatar;

    private Long cityId;
    private String cityName;

    private String nextScreen;
}
