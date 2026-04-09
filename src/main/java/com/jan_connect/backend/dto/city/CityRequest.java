package com.jan_connect.backend.dto.city;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityRequest {
    
    @NotBlank(message = "City name is required")
    private String name;

    @NotBlank(message = "StateID is required")
    private String stateId;

    // Not necessary parameters
    private String emoji;
    private String colorPrimary;
    private String colorSecondary;

    @NotNull(message = "Poll options are required")
    @Size(min = 2, max = 4, message = "A city poll must be bertween 2 and 4 options")
    private List<String> pollOptions;
}
