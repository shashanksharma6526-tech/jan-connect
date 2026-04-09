package com.jan_connect.backend.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    
    @NotNull(message = "Vote Direction is required")
    private Boolean upvote;
}
