package com.jan_connect.backend.dto.poll;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PollVoteRequest {
    
    @NotNull(message = "Option Index is required")
    @Size(min = 0, message = "Option Index must be non-negative")
    /*
        Use this if throws error:
        @Min(value = 0, message = "Option index must be 0 or greater")
        @Max(value = 3, message = "Option index cannot exceed 3")
     */
    private Integer optionIndex;
}
