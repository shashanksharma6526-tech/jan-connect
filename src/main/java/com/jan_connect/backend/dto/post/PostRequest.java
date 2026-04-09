package com.jan_connect.backend.dto.post;

import com.jan_connect.backend.enums.PostCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    
    @NotNull(message = "Post category is required")
    private PostCategory category;

    @NotBlank(message = "Post title is required")
    @Size(max = 200, message = "Post title must be atmost 200 characters")
    private String title;

    @NotBlank(message = "Post description is required")
    @Size(max = 2000, message = "Post description must be atmost 2000 characters")
    private String description;

    private String imageUrl;

    private String emoji;
}
