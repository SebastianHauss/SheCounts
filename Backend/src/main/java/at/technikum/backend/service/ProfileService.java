package at.technikum.backend.service;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> readAll() {
        return profileRepository.findAll();
    }

    public Profile read(UUID id) {
        if (checkIfProfileExists(id).isEmpty()) {
            throw new EntityNotFoundException("Profile not found.");
        }
        return checkIfProfileExists(id).get();
    }

    public Profile create(Profile profile) {
        if (checkIfProfileExists(profile.getId()).isPresent()) {
            throw new EntityAlreadyExistsException("Profile already exists.");
        }
        return profileRepository.save(profile);
    }

    @Transactional
    public Profile update(UUID id, Profile profile) {
        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found."));

        existing.setProfilePicUrl(profile.getProfilePicUrl());
        existing.setGender(profile.getGender());
        existing.setCountry(profile.getCountry());

        return profileRepository.save(existing);
    }

   /* @Transactional
    public void delete(UUID id) {
        if (checkIfProfileExists(id).isEmpty()) {
            throw new EntityNotFoundException("Profile not found.");
        }
        profileRepository.delete(checkIfProfileExists(id).get());
    }
       */
    public Optional<Profile> checkIfProfileExists(UUID id) {
        return profileRepository.findById(id);
    }
}
