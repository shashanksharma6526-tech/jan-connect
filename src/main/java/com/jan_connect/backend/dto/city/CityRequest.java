package com.jan_connect.backend.dto.city;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

// Used by SuperAdmin to create or update a city
@Data
public class CityRequest {

    @NotBlank(message = "City name is required")
    private String name;

    @NotNull(message = "State ID is required")
    private Long stateId;

    private String emoji;
    private String colorPrimary;
    private String colorSecondary;

    // Poll options for this city — between 2 and 4 options required
    @NotNull(message = "Poll options are required")
    @Size(min = 2, max = 4, message = "A city poll must have between 2 and 4 options")
    private List<String> pollOptions;
}