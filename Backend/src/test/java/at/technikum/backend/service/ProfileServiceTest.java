package at.technikum.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.enums.Gender;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Profile Service Unit Tests")
public class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    // ====================================================================
    // POSITIVE FÄLLE
    // ====================================================================

    @Test
    void readAll_shouldReturnProfiles() {
        // ---------- given ----------
        List<Profile> expectedProfiles = List.of(
                mock(Profile.class),
                mock(Profile.class));

        when(profileRepository.findAll()).thenReturn(expectedProfiles);

        // ---------- when ----------
        List<Profile> actualProfiles = profileService.readAll();

        // ---------- then ----------
        assertEquals(expectedProfiles, actualProfiles);
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void read_shouldReturnProfile_whenProfileExists() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();
        Profile expectedProfile = mock(Profile.class);

        when(profileRepository.findById(id)).thenReturn(Optional.of(expectedProfile));

        // ---------- when ----------
        Profile actualProfile = profileService.read(id);

        // ---------- then ----------
        assertEquals(expectedProfile, actualProfile);
        verify(profileRepository).findById(id);
    }

    @Test
    void create_shouldCreateNewProfile() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();
        Profile profile = mock(Profile.class);

        when(profile.getId()).thenReturn(id);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());
        when(profileRepository.save(profile)).thenReturn(profile);

        // ---------- when ----------
        Profile result = profileService.create(profile);

        // ---------- then ----------
        assertEquals(profile, result);
        verify(profileRepository).findById(id);
        verify(profileRepository).save(profile);
    }

    @Test
    void checkIfProfileExists_shouldReturnProfile_whenFound() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();
        Profile profile = mock(Profile.class);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        // ---------- when ----------
        Optional<Profile> result = profileService.checkIfProfileExists(id);

        // ---------- then ----------
        assertTrue(result.isPresent());
        assertEquals(profile, result.get());
        verify(profileRepository).findById(id);
    }

    @Test
    void update_shouldUpdateAndSaveProfile_whenProfileExists() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();

        Profile existing = new Profile();
        existing.setGender(Gender.MALE);
        existing.setCountry("AT");
        existing.setBirthday(LocalDate.of(1990, 1, 1));

        Profile updateData = new Profile();
        updateData.setGender(Gender.DIVERSE);
        updateData.setCountry("DE");
        updateData.setBirthday(LocalDate.of(2000, 2, 2));

        when(profileRepository.findById(id)).thenReturn(Optional.of(existing));
        when(profileRepository.save(existing)).thenReturn(existing);

        // ---------- when ----------
        Profile result = profileService.update(id, updateData);

        // ---------- then ----------
        assertEquals(Gender.DIVERSE, result.getGender());
        assertEquals("DE", result.getCountry());
        assertEquals(LocalDate.of(2000, 2, 2), result.getBirthday());

        verify(profileRepository).findById(id);
        verify(profileRepository).save(existing);
    }

    // ====================================================================
    // NEGATIVE FÄLLE
    // ====================================================================

    @Test
    void read_shouldThrowException_whenProfileDoesNotExist() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // ---------- when & then ----------
        assertThrows(EntityNotFoundException.class,
                () -> profileService.read(id));

        verify(profileRepository).findById(id);
    }

    @Test
    void create_shouldThrowException_whenProfileAlreadyExists() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();
        Profile profile = mock(Profile.class);

        when(profile.getId()).thenReturn(id);
        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        // ---------- when & then ----------
        assertThrows(EntityAlreadyExistsException.class, () -> profileService.create(profile));

        verify(profileRepository).findById(id);
        verify(profileRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenProfileDoesNotExist() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();
        Profile updateData = new Profile();

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // ---------- when & then ----------
        assertThrows(EntityNotFoundException.class,
                () -> profileService.update(id, updateData));

        verify(profileRepository).findById(id);
        verify(profileRepository, never()).save(any());
    }

    @Test
    void checkIfProfileExists_shouldReturnEmpty_whenNotFound() {
        // ---------- given ----------
        UUID id = UUID.randomUUID();

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // ---------- when ----------
        Optional<Profile> result = profileService.checkIfProfileExists(id);

        // ---------- then ----------
        assertTrue(result.isEmpty());
        verify(profileRepository).findById(id);
    }
}
