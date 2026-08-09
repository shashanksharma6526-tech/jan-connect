package com.jan_connect.backend.util;

import com.jan_connect.backend.entity.City;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ComplaintIdGenerator {

    private final CityRepository cityRepository;
    private final ComplaintRepository complaintRepository;

    // Generates the next CMP-XXX ID for a given city.
    // The sequence is per-city and is stored on the City entity itself.
    // @Transactional ensures the increment and save are atomic — no duplicate IDs
    // can be generated under concurrent requests.
    @Transactional
    public String generateNextId(City city) {
        int nextSequence = city.getComplaintSequence() + 1;

        // Persist the updated counter immediately before we use it
        city.setComplaintSequence(nextSequence);
        cityRepository.save(city);

        // Format: CMP-001, CMP-042, CMP-1000
        return String.format("CMP-%03d", nextSequence);
    }
}