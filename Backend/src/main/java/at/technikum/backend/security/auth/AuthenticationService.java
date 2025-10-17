package at.technikum.backend.security.auth;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.entity.User;
import at.technikum.backend.repository.UserRepository;
import at.technikum.backend.security.authdtos.AuthenticationRequest;
import at.technikum.backend.security.authdtos.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;
    private final Long jwtExpiryMs = 86400000L;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository repository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest loginRequest) {
        UserDetails userDetails = authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        String tokenValue = generateToken(userDetails);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(tokenValue)
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AuthenticationResponse> register(AuthenticationRequest request) {
        User user = new User();
        user.setEmail(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

//        //if a user is created a profile should be created as well
//        Profile profile = new Profile();
//        profile.setUser(user);
//        user.setProfile(profile);
        repository.save(user);


        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String tokenValue = generateToken(userDetails);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(tokenValue)
                .build();

        return ResponseEntity.ok(response);
    }


    @Override
    public UserDetails authenticate(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

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

    private String extractUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    private Key getSigningKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
