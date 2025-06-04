package at.technikum.backend.controller;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.service.ProfileService;
import jakarta.validation.Valid;
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

    @GetMapping
    public List<Profile> readAll() {
        return profileService.readAll();
    }

    @GetMapping("/{id}")
    public Profile read(@PathVariable UUID id) {
        return profileService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile create(@RequestBody @Valid Profile profile) {
        return profileService.create(profile);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Profile update(@PathVariable UUID id, @RequestBody @Valid Profile profile) {
        return profileService.update(id, profile);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        profileService.delete(id);
    }
}