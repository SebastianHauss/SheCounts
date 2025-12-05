package at.technikum.backend.security.auth;

import at.technikum.backend.security.authdtos.AuthenticationRequest;
import at.technikum.backend.security.authdtos.AuthenticationResponse;
import at.technikum.backend.security.authdtos.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("username", auth.getName());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest loginRequest,
            HttpServletResponse httpServletResponse) {
        return authenticationService.login(loginRequest, httpServletResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth_token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in production
        cookie.setPath("/");
        cookie.setMaxAge(0); // deletes the cookie
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
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
