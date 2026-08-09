package com.jan_connect.backend.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CursorUtil {

    public static String encode(int score, long id) {
        String raw = score + ":" + id;
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static CursorData decode(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        try {
            String decoded = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
            String[] parts = decoded.split(":");
            if (parts.length == 2) {
                return new CursorData(Integer.parseInt(parts[0]), Long.parseLong(parts[1]));
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public record CursorData(int score, long id) {}
}
