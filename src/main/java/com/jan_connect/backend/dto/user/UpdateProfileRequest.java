package com.jan_connect.backend.dto.user;

// import java.time.LocalDateTime;

// import com.jan_connect.backend.enums.AuthProvider;
// import com.jan_connect.backend.enums.UserType;

import jakarta.validation.constraints.Size;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
import lombok.Data;
// import lombok.NoArgsConstructor;

@Data
public class UpdateProfileRequest {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    private String avatar;

    private Long cityId;
}
