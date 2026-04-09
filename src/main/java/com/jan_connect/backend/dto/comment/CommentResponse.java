package com.jan_connect.backend.dto.comment;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    
    private Long id;
    private String content;

    private Long authorId;
    private String authorUsername;
    private String authorAvatar;

    private LocalDateTime createdAt;

    private Long parentCommentId;

    private List<CommentResponse> replies;
}
