package com.jan_connect.backend.dto.city;

import java.util.List;

import com.jan_connect.backend.dto.department.DepartmentResponse;
import com.jan_connect.backend.dto.poll.PollResponse;
// import com.jan_connect.backend.entity.Department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Full details when the app opens a page
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {
    
    private Long id;
    private String name;
    private String emoji;
    private String colorPrimary;
    private String colorSecondary;
    private String isActive;

    private Long stateID;
    private String stateName;

    private long totalPost;

    private PollResponse poll;

    private List<DepartmentResponse> departments;
}
