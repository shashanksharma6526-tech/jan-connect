package com.jan_connect.backend.dto;

import java.time.LocalDateTime;

import com.jan_connect.backend.entity.ComplaintCategory;
import com.jan_connect.backend.enums.ComplaintStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplaintResponse {
    
    private Long id;
    private String title;
    private String description;
    private String location;
    private Integer voteScore;
    private Long upvoteCount;
    private Long downvoteCount;
    private String authorName;
    private String communityCity;
    private String authorityNote;
    private LocalDateTime createdAt;
    private int commentCount;

    private ComplaintCategory category;
    private ComplaintStatus status;
}
