package com.civicpulse.controller;

import com.civicpulse.dto.state.StateResponse;
import com.civicpulse.service.StateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
@Tag(name = "Geography — States")
public class StateController {

    private final StateService stateService;

    @GetMapping
    @Operation(summary = "List all states with city count")
    public ResponseEntity<List<StateResponse>> getAllStates() {
        return ResponseEntity.ok(stateService.getAllStates());
    }

    @GetMapping("/{stateId}/cities")
    @Operation(summary = "List all active cities in a state")
    public ResponseEntity<?> getCitiesInState(@PathVariable Long stateId) {
        return ResponseEntity.ok(stateService.getCitiesInState(stateId));
    }
}