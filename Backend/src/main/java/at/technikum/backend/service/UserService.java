package at.technikum.backend.service;

import at.technikum.backend.entity.User;
import at.technikum.backend.exceptions.EmailAlreadyRegisteredException;
import at.technikum.backend.exceptions.UserNotFoundException;
import at.technikum.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User register(User user) {
        if (checkIfEmailExists(user.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("This email is already registered. Choose another email or login to your account.");
        }
        return userRepository.save(user);
    }

    public User read(String id) {
        return checkIfUserIdExists(id)
                .orElseThrow(() -> new EmailAlreadyRegisteredException("This email is already registered. Choose another email or login to your account."));
    }

    @Transactional
    public User update(User user) {
        if (checkIfUserIdExists(user.getId()).isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return userRepository.save(user);
    }


    @Transactional
    public void delete(User user) {
        if (checkIfUserIdExists(user.getId()).isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.delete(user);
    }


    public Optional<User> checkIfEmailExists(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> checkIfUserIdExists(String id) {
        return userRepository.findById(id);
    }

}
