package at.technikum.backend.service;

import at.technikum.backend.dto.UserDto;
import at.technikum.backend.entity.User;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityIdDoesNotMatchException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User read(UUID id) {
        return checkIfUserIdExists(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Transactional
    public User update(UUID id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setAdmin(user.isAdmin());

        return userRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        User user = checkIfUserIdExists(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        userRepository.delete(user);
    }

    public Optional<User> checkIfUserIdExists(UUID id) {
        return userRepository.findById(id);
    }
}