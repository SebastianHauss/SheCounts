package at.technikum.backend.service;

import at.technikum.backend.entity.RegisteredUser;
import at.technikum.backend.exceptions.EmailAlreadyRegisteredException;
import at.technikum.backend.exceptions.UserNotFoundException;
import at.technikum.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public RegisteredUser register(RegisteredUser user){
        Optional<RegisteredUser> checkIfUserExists = userRepository.findByEmail(user.getEmail());

        if(checkIfUserExists.isPresent()){
            throw new EmailAlreadyRegisteredException("This email is already registered. Choose another email or login to your account.");
        }

        return userRepository.save(user);
    }

    public RegisteredUser read(String id){
        Optional<RegisteredUser> findUserById = userRepository.findById(id);

        if(findUserById.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return findUserById.get();
    }

    public RegisteredUser update(RegisteredUser user){
        Optional<RegisteredUser> findUserById = userRepository.findById(user.getId());

        if (findUserById.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return userRepository.save(user);
    }

    public void delete(RegisteredUser user){
        Optional<RegisteredUser> findUserById = userRepository.findById(user.getId());

        if (findUserById.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        userRepository.delete(user);
    }

}
