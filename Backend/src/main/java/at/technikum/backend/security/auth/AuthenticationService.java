package at.technikum.backend.security.auth;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.entity.User;
import at.technikum.backend.enums.Gender;
import at.technikum.backend.repository.ProfileRepository;
import at.technikum.backend.repository.UserRepository;
import at.technikum.backend.security.authdtos.AuthenticationRequest;
import at.technikum.backend.security.authdtos.AuthenticationResponse;
import at.technikum.backend.security.authdtos.RegisterRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final Long jwtExpiryMs = 86400000L;
    @Value("${jwt.secret}")
    private String secretKey;

    public AuthenticationService(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            ProfileRepository profileRepository,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<AuthenticationResponse> login(
            AuthenticationRequest loginRequest,
            HttpServletResponse httpServletResponse) {
        UserDetails userDetails = authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword());

        String tokenValue = generateToken(userDetails);

        httpServletResponse.addCookie(createAuthCookie(tokenValue));

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token(tokenValue)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<AuthenticationResponse> register(
            RegisterRequest request,
            HttpServletResponse httpServletResponse) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // if a user is created a profile should be created as well
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setCountry(request.getCountry());

        String gender = request.getGender();
        if (gender.equalsIgnoreCase("female")) {
            profile.setGender(Gender.FEMALE);
        } else if (gender.equalsIgnoreCase("male")) {
            profile.setGender(Gender.MALE);
        } else {
            profile.setGender(Gender.DIVERSE);
        }

        profileRepository.save(profile);

        user.setProfile(profile);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String tokenValue = generateToken(userDetails);

        httpServletResponse.addCookie(createAuthCookie(tokenValue));

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token(tokenValue)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @Override
    public UserDetails authenticate(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return userDetailsService.loadUserByUsername(username);
    }

    @Override
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public UserDetails validateToken(String token) {
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Cookie createAuthCookie(String tokenValue) {
        Cookie cookie = new Cookie("auth_token", tokenValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO: change to true in production with HTTPS or use @value
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setDomain("localhost");
        cookie.setAttribute("SameSite", "Lax");
        return cookie;
    }
}