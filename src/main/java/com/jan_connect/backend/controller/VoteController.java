package com.jan_connect.backend.controller;

import com.jan_connect.backend.dto.post.VoteRequest;
import com.jan_connect.backend.service.RedisVoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/vote")
@RequiredArgsConstructor
@Tag(name = "Votes")
@SecurityRequirement(name = "bearerAuth")
public class VoteController {

    private final RedisVoteService redisVoteService;

    // Cast or toggle a vote — direction comes in body as { "direction": "UP" }
    @PostMapping
    @Operation(summary = "Cast or toggle a vote on a post")
    public ResponseEntity<RedisVoteService.VoteResult> castVote(
            @PathVariable Long postId,
            @Valid @RequestBody VoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
            redisVoteService.castVote(postId, request.getDirection(),
                userDetails.getUsername()));
    }

    // Remove vote entirely
    @DeleteMapping
    @Operation(summary = "Remove a vote from a post")
    public ResponseEntity<Void> removeVote(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        redisVoteService.removeVote(postId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}