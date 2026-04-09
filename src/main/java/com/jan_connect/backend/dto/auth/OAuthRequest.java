package com.jan_connect.backend.dto.auth;

import com.jan_connect.backend.enums.AuthProvider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
// import software.amazon.awssdk.services.s3.endpoints.internal.Value.Str;

@Data
public class OAuthRequest {
    
    @NotBlank(message = "Provider is required")
    private AuthProvider provider;

    @NotBlank(message = "Provider ID is required")
    private String providerId;

    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    

    private String avatarUrl;

    private Long cityId;
}
