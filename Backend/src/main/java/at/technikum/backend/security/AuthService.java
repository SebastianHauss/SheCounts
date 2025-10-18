package at.technikum.backend.security;

import at.technikum.backend.dto.RegisterRequest;
import at.technikum.backend.entity.User;
import at.technikum.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public UserDetails login(String email, String password) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return userDetails;



//        return userRepository.findByEmail(email)
//                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
//                .orElse(null);
    }

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAdmin(false);
        user.setCreatedAt(java.time.LocalDateTime.now());

        return userRepository.save(user);
    }
}