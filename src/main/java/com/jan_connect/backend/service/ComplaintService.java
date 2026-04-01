package com.jan_connect.backend.service;

import org.springframework.stereotype.Service;

import com.jan_connect.backend.dto.ComplaintRequest;
import com.jan_connect.backend.dto.ComplaintResponse;
import com.jan_connect.backend.entity.Community;
import com.jan_connect.backend.entity.Complaint;
import com.jan_connect.backend.entity.User;
import com.jan_connect.backend.exceptions.ResourceNotFoundExcepton;
import com.jan_connect.backend.repository.CommunityRepository;
import com.jan_connect.backend.repository.ComplaintRepository;
import com.jan_connect.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final RedisVoteService redisVoteService;

    pubic ComplaintResponse submitComplain
}
