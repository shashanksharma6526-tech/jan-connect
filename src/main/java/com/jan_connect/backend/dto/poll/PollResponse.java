package com.jan_connect.backend.dto.poll;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollResponse {
    
    private List<String> options;

    private Map<Integer, Long> voteCounts;

    private long totalVotes;

    private Integer userVotedIndex; // Guest or not voted yet

    private boolean userHasVoted;
}
