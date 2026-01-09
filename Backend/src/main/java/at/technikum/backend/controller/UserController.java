package at.technikum.backend.controller;

import at.technikum.backend.dto.UserDto;
import at.technikum.backend.entity.User;
import at.technikum.backend.mapper.UserMapper;
import at.technikum.backend.repository.UserRepository;
import at.technikum.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDto> readAll() {
        List<User> userList = userService.readAll();
        return userList.stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/me")
    public UserDto me(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return userMapper.toDto(user);
    }


    @GetMapping("/{id}")
    public UserDto read(@PathVariable UUID id) {
        return userMapper.toDto(userService.read(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable UUID id, @RequestBody @Valid UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}