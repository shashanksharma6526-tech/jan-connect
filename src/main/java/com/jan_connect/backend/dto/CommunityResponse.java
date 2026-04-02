package com.jan_connect.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommunityResponse {
    
    private Long id;
    private String city;
    private String province;
    private String country;
    private String description;
    private String isActive;
    private LocalDateTime createdAt;
    private int totalComplaints;
}
