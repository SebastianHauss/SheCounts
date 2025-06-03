package at.technikum.backend.controller;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile create(Profile profile) {
        return profileService.create(profile);
    }

    @GetMapping
    public List<Profile> readAll() {
        return profileService.readAll();
    }

    @GetMapping("/{id}")
    public Profile read(@PathVariable UUID id) {
        return profileService.read(id);
    }

    @PutMapping
    public Profile update(@RequestBody Profile profile) {
        return profileService.update(profile);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        profileService.delete(id);
    }
}
