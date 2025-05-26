package at.technikum.backend.repository;

import at.technikum.backend.entity.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<RegisteredUser, String> {

    Optional<RegisteredUser> findByEmail(String email);

}
