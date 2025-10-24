package at.technikum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProfileDto {

    private UUID id;
    private String profilePicUrl;
    private String gender;
    private String country;
    private UUID userId;
}
