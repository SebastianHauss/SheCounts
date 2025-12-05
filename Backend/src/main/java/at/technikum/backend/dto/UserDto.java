package at.technikum.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private boolean isAdmin;
    private UUID profileId;
}
