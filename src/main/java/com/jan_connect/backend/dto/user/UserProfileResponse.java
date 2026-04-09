package com.jan_connect.backend.dto.user;

import java.time.LocalDateTime;

import com.jan_connect.backend.enums.AuthProvider;
import com.jan_connect.backend.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    
    private Long userId;
    private String username;
    private String avatar;
    private UserType type;
    private AuthProvider provider;

    private Long cityId;
    private String cityName;

    private int karma;
    private long totalPosts;
    private long totalComplaints;

    private LocalDateTime joinedAt;
}
