package com.jan_connect.backend.dto.state;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateResponse {
    
    private Long id;
    private String name;
    private String emoji;
    private String colorPrimary;
    private String colorSecondary;

    private int cityCount;

    private List<StateCityItem> cities;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StateCityItem {
        private Long id;
        private String name;
        private String emoji;
        private String colorPrimary;
    }
}
