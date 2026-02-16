package at.technikum.backend.security.authdtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    private String gender;
    private String username;
    private String email;
    private String password;
    private String country;

}
