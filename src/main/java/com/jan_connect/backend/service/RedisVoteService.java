package com.jan_connect.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import com.jan_connect.backend.entity.Complaint;
import com.jan_connect.backend.entity.User;
import com.jan_connect.backend.exceptions.ResourceNotFoundExcepton;
import com.jan_connect.backend.repository.ComplaintRepository;
import com.jan_connect.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisVoteService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public static class VoteResult {
        public boolean success;
        public int scoreDelta;

        public VoteResult(boolean success, int scoreDelta) {
            this.success = success;
            this.scoreDelta = scoreDelta;
        }
    }

    private String upVoteKey(Long complaintId) {
        return "vote:up:" + complaintId;
    }

    private String downVoteKey(Long complaintId) {
        return "vote:down:" + complaintId;
    }

    @Transactional
    public VoteResult castVote(Long complaintId, boolean isUpVote, String userEmail) {

        User voter = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundExcepton("User not found"));

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundExcepton("Complaint not found with ID: " + complaintId));

        String userID = voter.getId().toString();
        String primaryKey = isUpVote ? upVoteKey(complaintId) : downVoteKey(complaintId);
        String oppositeKey = isUpVote ? downVoteKey(complaintId) : upVoteKey(complaintId);

        Boolean alreadyVotedSame = redisTemplate.opsForSet().isMember(primaryKey, userID);
        boolean alreadyUpvotedOpposite = redisTemplate.opsForSet().isMember(oppositeKey, userID);

        int scoreDelta = 0;

        if(Boolean.TRUE.equals(alreadyVotedSame)){
            redisTemplate.opsForSet().remove(primaryKey, userID);
            scoreDelta = isUpVote ? -1 : +1;
        }
        else if(Boolean.TRUE.equals(alreadyUpvotedOpposite)){
            redisTemplate.opsForSet().remove(oppositeKey, userID);
            redisTemplate.opsForSet().add(primaryKey, userID);
            scoreDelta = isUpVote ? +2 : -2;
        }
        else {
            redisTemplate.opsForSet().add(primaryKey, userID);
            scoreDelta = isUpVote ? +1 : -1;
        }
        
        return new VoteResult(true, scoreDelta);
    }
}