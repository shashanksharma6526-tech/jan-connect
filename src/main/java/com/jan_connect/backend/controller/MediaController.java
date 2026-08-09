package com.jan_connect.backend.controller;

import com.jan_connect.backend.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "Media Upload")
@SecurityRequirement(name = "bearerAuth")
public class MediaController {

    private final MediaService mediaService;

    // The Kotlin app uploads the image as multipart/form-data.
    // This returns the CDN URL which is then passed into the PostRequest.
    @PostMapping(value = "/image", consumes = "multipart/form-data")
    @Operation(summary = "Upload a post image — returns the CDN URL")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) throws Exception {

        String cdnUrl = mediaService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", cdnUrl));
    }
}