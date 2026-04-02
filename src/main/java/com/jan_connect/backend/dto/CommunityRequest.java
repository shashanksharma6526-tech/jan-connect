package com.jan_connect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommunityRequest {
    
    @NotBlank(message = "City name is required")
    private String city;

    // province, country, description
    @NotBlank(message = "Province name is required")
    private String province;

    @NotBlank(message = "City name is required")
    private String country;

    @NotBlank(message = "Description is required")
    private String description;
}
