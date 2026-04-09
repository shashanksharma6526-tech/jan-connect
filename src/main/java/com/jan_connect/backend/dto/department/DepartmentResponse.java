package com.jan_connect.backend.dto.department;

import com.jan_connect.backend.enums.DepartmentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    
    private Long id;
    private String label;
    private DepartmentType type;
    private String icon;
    private boolean isActive;
    private Long cityId;
}
