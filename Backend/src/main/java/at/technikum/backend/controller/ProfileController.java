package at.technikum.backend.controller;

import at.technikum.backend.entity.Profile;
import at.technikum.backend.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile create(Profile profile){
        return profileService.create(profile);
    }

    @GetMapping
    public List<Profile> readAll(){  //für den Admin
        return profileService.readAll();
    }

    @GetMapping("/{id}")
    public Profile read(@PathVariable String id){
        return profileService.read(id);
    }

    @PutMapping
    public Profile update(@RequestBody Profile profile){
        return profileService.update(profile);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        profileService.delete(id);
    }



}
