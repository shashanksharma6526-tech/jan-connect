package com.jan_connect.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jan_connect.backend.dto.ComplaintResponse;
import com.jan_connect.backend.repository.UserRepository;
import com.jan_connect.backend.service.ComplaintService;
import com.jan_connect.backend.service.RedisVoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/complaints")
@Tag(name = "Complaints", description = "Submit and browse civic complaints")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final RedisVoteService redisVoteService;
    private final UserRepository userRepository;

    @PostMapping
    @Operation(summary = "Submit a new complaint")
    public ResposeEntity<ComplaintResponse> submitComplaint(
            @PathVariable String city,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userID = resolveUserID(userDetails);
        return ResponseEntity.ok(
                complaintService.getCityFrontPaage(city, cursor, limit, userID));
    }

    @
    @GetMapping("/{id}")
    @Operation(summary = "Get full details of a single complaint")
    public ResponseEntity<ComplaintResponse> getComplaint(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(complaintService.getComplaintById(id, userDetails));
    }

    @PostMapping("/{id}/downvote")
    @Operation(summary = "Downvote a complaint (marks as spam or invalid)")
    public ResponseEntity<RedisVoteService.VoteResult> downvote(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(redisVoteService.castVote(id, false, userDetails.getUsername()));
    }

    // To resolve User ID from their email
    private Long resolveUserId(UserDetails userDetails){
        if(userDetails == null)
            return null;
        return userRepository.findByEmail(userDetails.getUsername())
            .map(User::getId)
            .orElse(null);
    }
}
