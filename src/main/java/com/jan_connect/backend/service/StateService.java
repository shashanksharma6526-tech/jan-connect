package com.jan_connect.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jan_connect.backend.dto.city.CityResponse;
import com.jan_connect.backend.dto.state.StateResponse;
import com.jan_connect.backend.entity.City;
import com.jan_connect.backend.entity.State;
import com.jan_connect.backend.exceptions.ResourceNotFoundException;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.StateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StateService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    public List<StateResponse> getAllStates() {
        return stateRepository.findAll().stream().map(state -> {
            List<StateResponse.StateCityItem> cityItems = state.getCities() != null
                    ? state.getCities().stream()
                            .filter(c -> Boolean.TRUE.equals(c.getIsActive()))
                            .map(c -> StateResponse.StateCityItem.builder()
                                    .id(c.getId())
                                    .name(c.getName())
                                    .emoji(c.getEmoji())
                                    .colorPrimary(c.getColourPrimary())
                                    .build())
                            .collect(Collectors.toList())
                    : List.of();

            return StateResponse.builder()
                    .id(state.getId())
                    .name(state.getName())
                    .emoji(state.getEmoji())
                    .colorPrimary(state.getColourPrimary())
                    .colorSecondary(state.getColourSecondary())
                    .cityCount(cityItems.size())
                    .cities(cityItems)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<CityResponse> getCitiesInState(Long stateId) {
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new ResourceNotFoundException("State not found with ID: " + stateId));

        return cityRepository.findByStateIdAndIsActiveTrueOrderByNameAsc(state.getId()).stream()
                .map(city -> CityResponse.builder()
                        .id(city.getId())
                        .name(city.getName())
                        .emoji(city.getEmoji())
                        .colorPrimary(city.getColourPrimary())
                        .colorSecondary(city.getColourSecondary())
                        .isActive(String.valueOf(city.getIsActive()))
                        .stateID(state.getId())
                        .stateName(state.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
