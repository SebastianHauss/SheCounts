package at.technikum.backend.service;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public List<Profile> readAll() {
        return profileRepository.findAll();
    }

    public Profile read(UUID id) {
        return checkIfProfileExists(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found."));
    }

    public Profile create(Profile profile) {
        UUID id = profile.getId();

        if (profileRepository.findById(id).isPresent()) {
            throw new EntityAlreadyExistsException("Profile already exists.");
        }

        return profileRepository.save(profile);
    }

    @Transactional
    public Profile update(UUID id, Profile profile) {
        log.info("ProfileService.update called with id: {}", id);

        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found."));

        log.debug("Existing profile - gender: {}, country: {}, birthday: {}",
                existing.getGender(), existing.getCountry(), existing.getBirthday());
        log.debug("New values - gender: {}, country: {}, birthday: {}",
                profile.getGender(), profile.getCountry(), profile.getBirthday());

        existing.setGender(profile.getGender());
        existing.setCountry(profile.getCountry());
        existing.setBirthday(profile.getBirthday());

        Profile saved = profileRepository.save(existing);
        log.info("Saved profile - gender: {}, country: {}, birthday: {}",
                saved.getGender(), saved.getCountry(), saved.getBirthday());

        return saved;
    }

    /*
     * @Transactional
     * public void delete(UUID id) {
     * if (checkIfProfileExists(id).isEmpty()) {
     * throw new EntityNotFoundException("Profile not found.");
     * }
     * profileRepository.delete(checkIfProfileExists(id).get());
     * }
     */
    public Optional<Profile> checkIfProfileExists(UUID id) {
        return profileRepository.findById(id);
    }
}
