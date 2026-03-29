package com.jan_connect.backend.util;

import org.springframework.stereotype.Component;

import com.jan_connect.backend.entity.City;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.ComplaintRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ComplaintIdGenerator {
    
    private final CityRepository cityRepository;
    private final ComplaintRepository complaintRepository;

    @Transactional
    public String generateNextId(City city){
        int nextSequence = city.getComplaintSequence() + 1;
        city.setComplaintSequence(nextSequence);
        cityRepository.save(city);

        return String.format("CMP-%03d", nextSequence);
    }
}
