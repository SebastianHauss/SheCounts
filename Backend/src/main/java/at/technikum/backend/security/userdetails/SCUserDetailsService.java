package at.technikum.backend.security.userdetails;

import at.technikum.backend.entity.User;
import at.technikum.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SCUserDetailsService implements UserDetailsService {

    // UserDetailsService ist dafür zuständig, einen Benutzer anhand des Usernamens
    // aus der Datenbank zu laden und für Spring Security in ein UserDetails-Objekt umzuwandeln

    private final UserRepository repository;

    public SCUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new SCUserDetails(user);
    }
}
