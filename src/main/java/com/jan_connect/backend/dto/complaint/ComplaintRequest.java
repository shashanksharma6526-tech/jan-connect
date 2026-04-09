package com.jan_connect.backend.dto.complaint;

import com.jan_connect.backend.enums.ComplaintPriority;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComplaintRequest {
    
    // name, contact, location, description, departmentId, priority
    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Contact cannot be null")
    private String contact;

    @NotNull(message = "Location cannot be null")
    private String location;

    @NotNull(message = "Description cannot be null")
    @Size(max = 1000, message = "Description cannot exceed 10000 characters")
    private String description;

    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    @NotNull(message = "Priority cannot be null")
    private ComplaintPriority priority;
}
