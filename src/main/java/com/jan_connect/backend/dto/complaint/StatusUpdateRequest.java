package com.jan_connect.backend.dto.complaint;

import com.jan_connect.backend.enums.ComplaintStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    
    @NotNull(message = "Status cannot be null")
    private ComplaintStatus status;

    private String authorityNote;
}
