package at.technikum.backend.security.auth;

import at.technikum.backend.security.authdtos.AuthenticationRequest;
import at.technikum.backend.security.authdtos.AuthenticationResponse;
import at.technikum.backend.security.authdtos.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest loginRequest,
            HttpServletResponse httpServletResponse) {
        return authenticationService.login(loginRequest, httpServletResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse httpServletResponse) {
        return authenticationService.register(request, httpServletResponse);
    }

    @PostMapping("/test")
    public String test() {
        return "The authentication endpoint is working!";
    }

}
