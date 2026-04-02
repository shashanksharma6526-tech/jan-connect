package com.jan_connect.backend.dto;

import lombok.Data;

@Data
public class CursorRequest {
    
    private String cursor;
    private int limit = 10;
}
