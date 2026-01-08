package at.technikum.backend.controller;

import at.technikum.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import at.technikum.backend.entity.User;
import at.technikum.backend.service.UserService;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    @PostMapping(value = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {
            String fileId = fileService.uploadProfilePicture(file);

            // Update user's profile picture ID
            String username = authentication.getName();
            userService.updateProfilePicture(username, fileId);

            return ResponseEntity.ok(Map.of(
                    "message", "Profile picture uploaded successfully",
                    "fileId", fileId
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload profile picture"));
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        try {
            InputStream fileStream = fileService.downloadFile(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Default, könnte dynamisch sein
                    .body(new InputStreamResource(fileStream));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/profile-picture")
    public ResponseEntity<?> deleteProfilePicture(Authentication authentication) {
        try {
            String username = authentication.getName();
            userService.updateProfilePicture(username, null);

            return ResponseEntity.ok(Map.of("message", "Profile picture removed successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to remove profile picture"));
        }
    }
}