package com.civicpulse.controller;

import com.civicpulse.dto.common.CursorPage;
import com.civicpulse.dto.post.*;
import com.civicpulse.entity.User;
import com.civicpulse.repository.UserRepository;
import com.civicpulse.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Posts — Feed")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    // ── GET FEED (Public — guests can scroll) ────────────────────────────
    @GetMapping("/api/cities/{cityId}/posts")
    @Operation(summary = "Paginated city feed with cursor-based infinite scroll")
    public ResponseEntity<CursorPage<PostResponse>> getCityFeed(
            @PathVariable Long cityId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(
            postService.getCityFeed(cityId, cursor, limit, userId));
    }

    // ── CREATE POST (Auth required) ───────────────────────────────────────
    @PostMapping("/api/cities/{cityId}/posts")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new post in a city")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Long cityId,
            @Valid @RequestBody PostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return new ResponseEntity<>(
            postService.createPost(cityId, request, userDetails.getUsername()),
            HttpStatus.CREATED);
    }

    // ── GET SINGLE POST ───────────────────────────────────────────────────
    @GetMapping("/api/posts/{postId}")
    @Operation(summary = "Get a single post with its comments")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = resolveUserId(userDetails);
        return ResponseEntity.ok(postService.getPostById(postId, userId));
    }

    // ── DELETE POST ───────────────────────────────────────────────────────
    @DeleteMapping("/api/posts/{postId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete own post (or admin)")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        postService.deletePost(postId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    private Long resolveUserId(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByEmail(userDetails.getUsername())
            .map(User::getId).orElse(null);
    }
}