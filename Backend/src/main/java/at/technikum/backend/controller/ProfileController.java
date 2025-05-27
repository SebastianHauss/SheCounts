package at.technikum.backend.controller;

import at.technikum.backend.entity.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(){
        return;
    }

    @GetMapping
    public void readAll(){  //für den Admin
        return;
    }

    @GetMapping("/{id}")
    public void read(@PathVariable String id){
        return;
    }

    @PutMapping
    public void update(@RequestBody Profile profile){
        return;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        return;
    }



}
