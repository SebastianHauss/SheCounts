package at.technikum.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import at.technikum.backend.storage.FileStorage;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileService Unit Tests")
public class FileServiceTest {

    @Mock
    private FileStorage fileStorage;

    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService(fileStorage);
    }

    @Test
    void uploadProfilePicture_shouldUpload_whenFileIsValid() {
        byte[] content = "test".getBytes();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                "image/jpeg",
                content);

        when(fileStorage.upload(file)).thenReturn("file-id");

        String result = fileService.uploadProfilePicture(file);

        assertEquals("file-id", result);
        verify(fileStorage).upload(file);
    }

    @Test
    void uploadProfilePicture_shouldThrow_whenFileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                "image/jpeg",
                new byte[0]);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fileService.uploadProfilePicture(file));

        assertEquals("File is empty", ex.getMessage());
        verifyNoInteractions(fileStorage);
    }

    @Test
    void uploadProfilePicture_shouldThrow_whenFileTooLarge() {
        byte[] content = new byte[(int) (20 * 1024 * 1024 + 1)];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                "image/jpeg",
                content);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fileService.uploadProfilePicture(file));

        assertEquals("File size exceeds maximum allowed size of 20MB", ex.getMessage());
        verifyNoInteractions(fileStorage);
    }

    @Test
    void uploadProfilePicture_shouldThrow_whenContentTypeInvalid() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.txt",
                "text/plain",
                "test".getBytes());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fileService.uploadProfilePicture(file));

        assertEquals("Invalid file type. Only JPEG, PNG, GIF and WebP are allowed", ex.getMessage());
        verifyNoInteractions(fileStorage);
    }

    @Test
    void uploadProfilePicture_shouldThrow_whenContentTypeNull() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profile.jpg",
                null,
                "test".getBytes());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> fileService.uploadProfilePicture(file));

        assertEquals("Invalid file type. Only JPEG, PNG, GIF and WebP are allowed", ex.getMessage());
        verifyNoInteractions(fileStorage);
    }

    @Test
    void downloadFile_shouldReturnStreamFromStorage() {
        InputStream expected = new ByteArrayInputStream("data".getBytes());
        when(fileStorage.load("123")).thenReturn(expected);

        InputStream result = fileService.downloadFile("123");

        assertEquals(expected, result);
        verify(fileStorage).load("123");
    }
}
