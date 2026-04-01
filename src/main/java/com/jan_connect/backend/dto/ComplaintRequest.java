package com.jan_connect.backend.dto;

import com.jan_connect.backend.entity.ComplaintCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComplaintRequest {
    
    @NotBlank(message = "Complaint title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private ComplaintCategory category;

    @NotBlank(message = "Problem location is required")
    private String location;
}
