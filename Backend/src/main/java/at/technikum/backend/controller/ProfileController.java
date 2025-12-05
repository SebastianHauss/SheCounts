package at.technikum.backend.controller;

import at.technikum.backend.dto.ProfileDto;
import at.technikum.backend.entity.Profile;
import at.technikum.backend.mapper.ProfileMapper;
import at.technikum.backend.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @GetMapping
    public List<ProfileDto> readAll() {
        List<Profile> profileList = profileService.readAll();
        return profileList.stream().map(profileMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ProfileDto read(@PathVariable UUID id) {
        return profileMapper.toDto(profileService.read(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileDto create(@RequestBody @Valid ProfileDto profileDto) {
        Profile profile = profileMapper.toEntity(profileDto);
        return profileMapper.toDto(profileService.create(profile));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileDto update(@PathVariable UUID id, @RequestBody @Valid ProfileDto profileDto) {
        Profile profile = profileMapper.toEntity(profileDto);
        return profileMapper.toDto(profileService.update(id, profile));
    }

    /*@DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        profileService.delete(id);
    }*/
}