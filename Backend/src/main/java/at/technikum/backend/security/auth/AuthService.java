package at.technikum.backend.security.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    //Login prüfen
    UserDetails authenticate(String username, String password);

    //Token für erfolgreichen Login erstellen
    String generateToken(UserDetails userDetails);

    //Token prüfen und Benutzer zurückliefern
    UserDetails validateToken(String token);

}
