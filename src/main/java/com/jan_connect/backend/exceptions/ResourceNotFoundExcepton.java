package com.jan_connect.backend.exceptions;

public class ResourceNotFoundExcepton extends RuntimeException {
    
    public ResourceNotFoundExcepton(String message){
        super(message);
    }
}
