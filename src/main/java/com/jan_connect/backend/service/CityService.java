package com.jan_connect.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jan_connect.backend.dto.city.CityRequest;
import com.jan_connect.backend.dto.city.CityResponse;
import com.jan_connect.backend.dto.department.DepartmentResponse;
import com.jan_connect.backend.entity.City;
import com.jan_connect.backend.entity.State;
import com.jan_connect.backend.exceptions.ResourceNotFoundException;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.StateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final ObjectMapper objectMapper;

    public List<CityResponse> getAllCitiesForAdmin() {
        return cityRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CityResponse getCityById(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + cityId));
        return mapToResponse(city);
    }

    @Transactional
    public CityResponse createCity(CityRequest request) {
        State state = stateRepository.findById(request.getStateId())
                .orElseThrow(() -> new ResourceNotFoundException("State not found with ID: " + request.getStateId()));

        String pollOptionsJson;
        try {
            pollOptionsJson = objectMapper.writeValueAsString(request.getPollOptions());
        } catch (Exception e) {
            pollOptionsJson = "[]";
        }

        City city = City.builder()
                .name(request.getName())
                .state(state)
                .emoji(request.getEmoji())
                .colourPrimary(request.getColorPrimary())
                .colourSecondary(request.getColorSecondary())
                .pollOptions(pollOptionsJson)
                .isActive(true)
                .complaintSequence(0)
                .build();

        return mapToResponse(cityRepository.save(city));
    }

    @Transactional
    public CityResponse updateCity(Long cityId, CityRequest request) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + cityId));

        if (request.getName() != null) {
            city.setName(request.getName());
        }
        if (request.getEmoji() != null) {
            city.setEmoji(request.getEmoji());
        }
        if (request.getColorPrimary() != null) {
            city.setColourPrimary(request.getColorPrimary());
        }
        if (request.getColorSecondary() != null) {
            city.setColourSecondary(request.getColorSecondary());
        }
        if (request.getStateId() != null) {
            State state = stateRepository.findById(request.getStateId())
                    .orElseThrow(() -> new ResourceNotFoundException("State not found with ID: " + request.getStateId()));
            city.setState(state);
        }
        if (request.getPollOptions() != null) {
            try {
                city.setPollOptions(objectMapper.writeValueAsString(request.getPollOptions()));
            } catch (Exception ignored) {
            }
        }

        return mapToResponse(cityRepository.save(city));
    }

    private CityResponse mapToResponse(City city) {
        List<DepartmentResponse> deptResponses = city.getDepartments() != null
                ? city.getDepartments().stream()
                        .map(d -> DepartmentResponse.builder()
                                .id(d.getId())
                                .label(d.getLabel())
                                .type(d.getType())
                                .icon(d.getIcon())
                                .isActive(d.getIsActive())
                                .cityId(city.getId())
                                .build())
                        .collect(Collectors.toList())
                : List.of();

        long postCount = city.getPosts() != null ? city.getPosts().size() : 0;

        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .emoji(city.getEmoji())
                .colorPrimary(city.getColourPrimary())
                .colorSecondary(city.getColourSecondary())
                .isActive(String.valueOf(city.getIsActive()))
                .stateID(city.getState() != null ? city.getState().getId() : null)
                .stateName(city.getState() != null ? city.getState().getName() : null)
                .totalPost(postCount)
                .departments(deptResponses)
                .build();
    }
}
