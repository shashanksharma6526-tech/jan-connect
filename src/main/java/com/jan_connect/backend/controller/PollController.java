package com.civicpulse.controller;

import com.civicpulse.dto.poll.*;
import com.civicpulse.entity.User;
import com.civicpulse.repository.UserRepository;
import com.civicpulse.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities/{cityId}/poll")
@RequiredArgsConstructor
@Tag(name = "City Poll")
public class PollController {

    private final PollService pollService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get poll options and current vote counts for a city")
    public ResponseEntity<PollResponse> getPoll(
            @PathVariable Long cityId,
            @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        Long userId = userDetails != null
            ? userRepository.findByEmail(userDetails.getUsername())
                .map(User::getId).orElse(null)
            : null;

        return ResponseEntity.ok(pollService.getPoll(cityId, userId));
    }

    @PostMapping("/vote")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Submit a poll vote — one vote per user per city")
    public ResponseEntity<PollResponse> vote(
            @PathVariable Long cityId,
            @RequestBody PollVoteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        return ResponseEntity.ok(
            pollService.submitPollVote(cityId, request, userDetails.getUsername()));
    }
}