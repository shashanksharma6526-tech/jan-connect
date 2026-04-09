package com.jan_connect.backend.dto.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursorPage<T> {
    
    private List<T> items;

    // Opaque Base64-encoded string encoding (voteScore, id) of the last item.
    // Null when there are no more items to load — signals end of feed.
    private String nextCursor;
    private boolean hasMore;

    private int fetchedCount;
}
