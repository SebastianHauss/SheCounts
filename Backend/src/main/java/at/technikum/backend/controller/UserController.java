package at.technikum.backend.controller;

import at.technikum.backend.entity.RegisteredUser;
import at.technikum.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public RegisteredUser register(RegisteredUser user){
        return userService.register(user);
    }

    @GetMapping
    public void read(){

    }

    @PutMapping
    public void update(){

    }

    @DeleteMapping
    public void delete(){

    }





}
