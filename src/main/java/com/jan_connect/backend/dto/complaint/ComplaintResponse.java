package com.jan_connect.backend.dto.complaint;

import java.time.LocalDateTime;

import com.jan_connect.backend.enums.ComplaintPriority;
import com.jan_connect.backend.enums.ComplaintStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintResponse {
    
    private Long id;
    private String complaintNumber;

    private String name;
    private String contact;
    private String description;
    private String location;

    private ComplaintPriority priority;
    private ComplaintStatus status;

    private String authorityNote;

    private Long departmentId;
    private String departmentLabel;
    private String departmentIcon;

    // Complaint done by:
    private Long cityId;
    private String cityName;

    // Complaint done by:
    private Long submittedById;
    private String submittedByUsername;

    private LocalDateTime createdAt;
}
