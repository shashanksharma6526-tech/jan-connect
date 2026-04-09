package com.jan_connect.backend.dto.common;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private int statusCode;
    private String message;

    private Map<String, String> fieldErrors;

    private LocalDateTime timeStamp;

    public ErrorResponse(int statusCode, String message, LocalDateTime timestamp){

        this.statusCode = statusCode;
        this.message = message;
        this.timeStamp = timestamp;
        this.fieldErrors = null;
    }
}
