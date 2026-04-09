package com.jan_connect.backend.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.jan_connect.backend.dto.comment.CommentResponse;
import com.jan_connect.backend.enums.PostCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    
    private Long id;
    private PostCategory category;
    private String title;
    private String description;
    private String imageUrl;
    private String emoji;

    private Integer voteScore;
    private Long upvoteCount;
    private Long downVoteCount;

    private boolean userUpvoted;
    private boolean userDownvoted;

    private Long authorId;
    private String authorUsername;
    private String authorAvatar;

    private Long cityId;
    private String cityName;

    private LocalDateTime createdAt;
    private int commentCount;

    private List<CommentResponse> comments;
}
