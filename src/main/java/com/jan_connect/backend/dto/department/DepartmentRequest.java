package com.jan_connect.backend.dto.department;

import com.jan_connect.backend.enums.DepartmentType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentRequest {
    
    @NotNull(message = "Department label is required")
    private String label;

    @NotNull(message = "Department type is required")
    private DepartmentType type;

    // App me dikhne wala icon
    private String icon;

    @NotNull(message = "City ID is required")
    private Long cityId;
}
