package com.jan_connect.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jan_connect.backend.dto.department.DepartmentResponse;
import com.jan_connect.backend.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getDepartmentsForCity(Long cityId) {
        return departmentRepository.findByCityIdAndIsActiveTrueOrderByLabelAsc(cityId).stream()
                .map(d -> DepartmentResponse.builder()
                        .id(d.getId())
                        .label(d.getLabel())
                        .type(d.getType())
                        .icon(d.getIcon())
                        .isActive(d.getIsActive())
                        .cityId(cityId)
                        .build())
                .collect(Collectors.toList());
    }
}
