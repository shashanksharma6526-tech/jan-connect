package com.jan_connect.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CursorPage<T> {
    
    // Complaint data
    private List<T> items;
    
    private String nextCursor;

    // To load more page    
    private Boolean hasMore;

    // Items fetched
    private int fetchedCount;
}
