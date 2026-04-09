package com.jan_connect.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Full name is required")
    @Size(min = 4, max = 50, message = "Full name must be at least 4 characters long")
    private String fullName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "City is required")
    private String city;;

    @NotBlank(message = "Locality is required")
    private String locality;
}
