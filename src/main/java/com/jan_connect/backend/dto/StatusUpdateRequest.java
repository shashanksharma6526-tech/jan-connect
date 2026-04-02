package com.jan_connect.backend.dto;

import com.jan_connect.backend.enums.ComplaintStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    
    @NotNull(message = "Status is required")
    private ComplaintStatus status;

    // from Authority
    private String authorityNote;
}
