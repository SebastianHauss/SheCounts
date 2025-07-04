package at.technikum.backend;

import at.technikum.backend.entity.User;
import at.technikum.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userInitializer(UserRepository userRepository) {
        return args -> {
            User admin = new User ("admin", "admin@shecounts.at", "admin123");
            admin.setAdmin(true);

            User user1 = new User("emily", "emily@example.com", "emily123");
            User user2 = new User("alex", "alex@example.com", "alex123");

            userRepository.saveAll(List.of(admin, user1, user2));
        };
    }


}
