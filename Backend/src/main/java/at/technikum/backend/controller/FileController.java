package at.technikum.backend.controller;

import at.technikum.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import at.technikum.backend.service.UserService;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        String username = authentication.getName();
        log.info("User '{}' attempting to upload profile picture. File size: {} bytes, Content-Type: {}",
                username, file.getSize(), file.getContentType());

        try {
            String fileId = fileService.uploadProfilePicture(file);
            log.debug("Profile picture uploaded to storage with fileId: {}", fileId);

            // Update user's profile picture ID;
            userService.updateProfilePicture(username, fileId);

            return ResponseEntity.ok(Map.of(
                    "message", "Profile picture uploaded successfully",
                    "fileId", fileId
            ));

        } catch (IllegalArgumentException e) {
            log.warn("Invalid profile picture upload attempt by user '{}': {}", username, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to upload profile picture for user '{}': {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload profile picture"));
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        log.debug("Attempting to download file with ID: {}", fileId);

        try {
            InputStream fileStream = fileService.downloadFile(fileId);
            log.info("File '{}' successfully retrieved for download", fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Default, könnte dynamisch sein
                    .body(new InputStreamResource(fileStream));

        } catch (Exception e) {
            log.warn("File '{}' not found or could not be downloaded: {}", fileId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<?> deleteProfilePicture(Authentication authentication) {
        String username = authentication.getName();
        log.info("User '{}' attempting to delete profile picture", username);
        try {
            userService.updateProfilePicture(username, null);
            log.info("Profile picture successfully removed for user '{}'", username);

            return ResponseEntity.ok(Map.of("message", "Profile picture removed successfully"));

        } catch (Exception e) {
            log.error("Failed to remove profile picture for user '{}': {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to remove profile picture"));
        }
    }
}