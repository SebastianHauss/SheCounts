package at.technikum.backend.controller;

import at.technikum.backend.entity.RegisteredUser;
import at.technikum.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisteredUser register(RegisteredUser user) {
        return userService.register(user);
    }

    @GetMapping("/{id}")
    public RegisteredUser read(@PathVariable String id) {
        return userService.read(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RegisteredUser update(RegisteredUser user) {
        return userService.update(user);
    }

    @DeleteMapping
    public void delete(RegisteredUser user) {
        userService.delete(user);
    }

}
