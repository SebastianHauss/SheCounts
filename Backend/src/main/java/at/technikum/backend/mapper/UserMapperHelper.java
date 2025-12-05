package at.technikum.backend.mapper;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapperHelper {

    private final ProfileRepository profileRepository;

    @Named("getUUIDFromProfile")
    public UUID getUUIDFromProfile(Profile profile){
        return profile != null ? profile.getId() : null;
    }
    @Named("getProfileFromUUID")
    public Profile getProfileFromUUID(UUID profileId){
        if (profileId == null) return null;

        Optional<Profile> profile =  profileRepository.findById(profileId);
        return profile.orElse(null);

    }
}
