package at.technikum.backend.service;

import at.technikum.backend.entity.User;
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


    public User register(User user){
        Optional<User> checkIfUserExists = userRepository.findByEmail(user.getEmail());

        if(checkIfUserExists.isPresent()){
            throw new EmailAlreadyRegisteredException("This email is already registered. Choose another email or login to your account.");
        }

        return userRepository.save(user);
    }

    //TODO: statt findUserById.isEmpty -> .orElseThrow(new ...) - ist eleganter
    public User read(String id){
        Optional<User> findUserById = userRepository.findById(id);

        if(findUserById.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return findUserById.get();
    }

    /* TODO: @Transactional
    Da update() und delete() Daten verändern, ist es best practice,
    sie mit @Transactional zu versehen, damit bei einem Fehler rollback erfolgt
     */
    //TODO: statt findUserById.isEmpty lieber .orElseThrow(() -> new ...) - ist eleganter
    public User update(User user){
        Optional<User> findUserById = userRepository.findById(user.getId());

        if (findUserById.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return userRepository.save(user);
    }


    /*
    TODO: Sollte deleteById(id) sein - REST löscht normalerweise per ID und das spart sich einen Datenbank-Call zum Laden des gesamten Objekts
     */
    // TODO: @Transactional
    public void delete(User user){
        Optional<User> findUserById = userRepository.findById(user.getId());

        if (findUserById.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        userRepository.delete(user);
    }

    // TODO: Optional<RegisteredUser> checkIfExists(String email) Methode
    // TODO: Optional<RegisteredUser> checkIfExists(id) Methode
}
