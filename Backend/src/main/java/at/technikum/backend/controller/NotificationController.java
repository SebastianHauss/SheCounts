package at.technikum.backend.controller;

import at.technikum.backend.entity.Notification;
import at.technikum.backend.entity.Profile;
import at.technikum.backend.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestBody Notification notification){
        return notificationService.create(notification);
    }

    @GetMapping
    public List<Notification> readAll(){  //für den Admin
        return notificationService.readAll();
    }

    @GetMapping("/{id}")
    public Notification read(@PathVariable String id){
        return notificationService.read(id);
    }

    @PutMapping
    public Notification update(@RequestBody Notification notification){
        return notificationService.update(notification);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        notificationService.delete(id);
    }



}
