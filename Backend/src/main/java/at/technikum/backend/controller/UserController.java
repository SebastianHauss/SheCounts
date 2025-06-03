package at.technikum.backend.controller;

import at.technikum.backend.entity.User;
import at.technikum.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User register(User user) {
        return userService.register(user);
    }

    @GetMapping("/{id}")
    public User read(@PathVariable UUID id) {
        return userService.read(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(User user) {
        return userService.update(user);
    }

    @DeleteMapping
    public void delete(User user) {
        userService.delete(user);
    }
}