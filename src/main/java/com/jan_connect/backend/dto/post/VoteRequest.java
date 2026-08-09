package com.jan_connect.backend.dto.post;

import com.jan_connect.backend.enums.VoteDirection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotNull(message = "Vote direction is required")
    private VoteDirection direction;
}
