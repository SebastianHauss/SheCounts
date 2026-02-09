package at.technikum.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import at.technikum.backend.entity.User;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void readAll_shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.readAll();

        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void read_shouldReturnUser_whenExists() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.read(id);

        assertEquals(user, result);
    }

    @Test
    void read_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.read(id));
    }

    @Test
    void update_shouldUpdateFields_andSave() {
        UUID id = UUID.randomUUID();

        User existing = new User();
        existing.setUsername("old");
        existing.setEmail("old@mail.com");
        existing.setAdmin(false);

        User update = new User();
        update.setUsername("new");
        update.setEmail("new@mail.com");
        update.setAdmin(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.update(id, update);

        assertEquals("new", result.getUsername());
        assertEquals("new@mail.com", result.getEmail());
        assertTrue(result.isAdmin());

        verify(userRepository).save(existing);
    }

    @Test
    void update_shouldThrow_whenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.update(id, new User()));
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_shouldDelete_whenExists() {
        UUID id = UUID.randomUUID();
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.delete(id);

        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.delete(id));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void updateProfilePicture_shouldSetFileId_andSave() {
        String email = "test@mail.com";
        User user = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.updateProfilePicture(email, "file-123");

        assertEquals("file-123", user.getProfilePictureId());
        verify(userRepository).save(user);
    }

    @Test
    void updateProfilePicture_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.updateProfilePicture("x", "file"));
    }

    @Test
    void getProfilePictureId_shouldReturnId_whenExists() {
        String email = "test@mail.com";
        User user = new User();
        user.setProfilePictureId("file-123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        String result = userService.getProfilePictureId(email);

        assertEquals("file-123", result);
    }

    @Test
    void getProfilePictureId_shouldReturnNull_whenUserNotFound() {
        when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

        String result = userService.getProfilePictureId("x");

        assertNull(result);
    }
}
