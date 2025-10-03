package at.technikum.backend;

import at.technikum.backend.entity.User;
import at.technikum.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User("admin", "admin@shecounts.at", passwordEncoder.encode("admin123"));
                admin.setAdmin(true);

                User user1 = new User("emily", "emily@example.com", passwordEncoder.encode("emily123"));
                User user2 = new User("alex", "alex@example.com", passwordEncoder.encode("alex123"));

                userRepository.saveAll(List.of(admin, user1, user2));
            }
        };
    }

}
