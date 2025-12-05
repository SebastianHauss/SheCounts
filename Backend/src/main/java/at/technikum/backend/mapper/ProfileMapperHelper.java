package at.technikum.backend.mapper;

import at.technikum.backend.dto.ProfileDto;
import at.technikum.backend.entity.Profile;
import at.technikum.backend.entity.User;
import at.technikum.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileMapperHelper {
    private final UserRepository userRepository;

    @Named("getUUIDFromUser")
    public UUID getUUIDFromUser(User user){
        return user != null ? user.getId() : null;
    }

    @Named("getUserFromUUID")
    public User getUserFromUUID(UUID userId){
        if (userId == null) return null;

        return userRepository.findById(userId).orElse(null);
    }
}
