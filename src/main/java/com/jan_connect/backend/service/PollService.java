package com.jan_connect.backend.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jan_connect.backend.dto.poll.PollResponse;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.PollVoteRepository;
import com.jan_connect.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PollService {
    
    private final CityRepository cityRepository;
    private final PollVoteRepository pollVoteRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public PollResponse getPoll(Long cityId, Long requestingUserId) throws Exception{

        
    }
}
