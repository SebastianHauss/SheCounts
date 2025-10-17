package at.technikum.backend.controller;

import at.technikum.backend.dto.LoginRequest;
import at.technikum.backend.dto.RegisterRequest;
import at.technikum.backend.entity.User;
import at.technikum.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping()
public class AuthController {

//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        User user = authService.login(request.getEmail(), request.getPassword());
//
//        if (user == null) {
//            return ResponseEntity.status(401).body("Invalid email or password");
//        }
//
//        return ResponseEntity.ok(user.getId());
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        User newUser = authService.register(request);
//        return ResponseEntity.ok(newUser.getId());
//    }
}
