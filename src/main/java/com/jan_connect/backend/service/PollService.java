package com.civicpulse.service;

import com.civicpulse.dto.poll.*;
import com.civicpulse.entity.*;
import com.civicpulse.exception.ResourceNotFoundException;
import com.civicpulse.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PollService {

    private final CityRepository cityRepository;
    private final PollVoteRepository pollVoteRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // Get poll options and current vote counts for a city
    public PollResponse getPoll(Long cityId, Long requestingUserId) throws Exception {
        City city = cityRepository.findByIdAndIsActiveTrue(cityId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "City not found with ID: " + cityId));

        List<String> options = objectMapper.readValue(
            city.getPollOptions(), new TypeReference<List<String>>() {}
        );

        // Build vote count map: { optionIndex → count }
        Map<Integer, Long> voteCounts = new HashMap<>();
        for (int i = 0; i < options.size(); i++) voteCounts.put(i, 0L);

        pollVoteRepository.countVotesByOptionForCity(cityId).forEach(row -> {
            int index = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            voteCounts.put(index, count);
        });

        // Has the requesting user already voted?
        Integer userVotedIndex = null;
        if (requestingUserId != null) {
            userVotedIndex = pollVoteRepository
                .findByVoterIdAndCityId(requestingUserId, cityId)
                .map(PollVote::getOptionIndex)
                .orElse(null);
        }

        return new PollResponse(options, voteCounts, userVotedIndex);
    }

    // Submit a poll vote — one per user per city (DB constraint enforced)
    @Transactional
    public PollResponse submitPollVote(
            Long cityId, PollVoteRequest request, String userEmail) throws Exception {

        User voter = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        City city = cityRepository.findByIdAndIsActiveTrue(cityId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "City not found with ID: " + cityId));

        List<String> options = objectMapper.readValue(
            city.getPollOptions(), new TypeReference<List<String>>() {}
        );

        if (request.getOptionIndex() < 0 || request.getOptionIndex() >= options.size()) {
            throw new IllegalArgumentException("Invalid option index.");
        }

        if (pollVoteRepository.findByVoterIdAndCityId(voter.getId(), cityId).isPresent()) {
            throw new RuntimeException("You have already voted in this city's poll.");
        }

        pollVoteRepository.save(PollVote.builder()
            .voter(voter)
            .city(city)
            .optionIndex(request.getOptionIndex())
            .build());

        return getPoll(cityId, voter.getId());
    }
}