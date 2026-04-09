package com.jan_connect.backend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    
    @NotBlank(message = "Comment section cannot be empty")
    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    private String content;

    // In case this comment is a comment of another comment
    private Long parentCommentId;
}
