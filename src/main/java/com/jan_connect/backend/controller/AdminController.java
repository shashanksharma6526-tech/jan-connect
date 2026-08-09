package com.jan_connect.backend.controller;

import com.jan_connect.backend.dto.city.*;
import com.jan_connect.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final CityService cityService;
    private final AdminService adminService;

    // ── CITY MANAGEMENT ───────────────────────────────────────────────────
    @GetMapping("/cities")
    @PreAuthorize("hasAnyRole('CITY_ADMIN','SUPER_ADMIN')")
    @Operation(summary = "List all cities for management")
    public ResponseEntity<List<CityResponse>> listCities() {
        return ResponseEntity.ok(cityService.getAllCitiesForAdmin());
    }

    @PostMapping("/cities")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Add a new city (SuperAdmin only)")
    public ResponseEntity<CityResponse> addCity(
            @Valid @RequestBody CityRequest request) {
        return new ResponseEntity<>(cityService.createCity(request), HttpStatus.CREATED);
    }

    @PatchMapping("/cities/{cityId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Edit city metadata (SuperAdmin only)")
    public ResponseEntity<CityResponse> updateCity(
            @PathVariable Long cityId,
            @Valid @RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.updateCity(cityId, request));
    }

    // ── COMPLAINTS (all cities) ───────────────────────────────────────────
    @GetMapping("/complaints")
    @PreAuthorize("hasAnyRole('CITY_ADMIN','SUPER_ADMIN')")
    @Operation(summary = "View all complaints across all cities")
    public ResponseEntity<?> getAllComplaints() {
        return ResponseEntity.ok(adminService.getAllComplaints());
    }

    // ── POST MODERATION ───────────────────────────────────────────────────
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasAnyRole('CITY_ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Moderate and remove any post")
    public ResponseEntity<Void> moderatePost(@PathVariable Long postId) {
        adminService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // ── USER MANAGEMENT ───────────────────────────────────────────────────
    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "List all users and manage roles (SuperAdmin only)")
    public ResponseEntity<?> listUsers(
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(adminService.listUsers(type));
    }
}