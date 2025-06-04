package at.technikum.backend.controller;

import at.technikum.backend.entity.Notification;
import at.technikum.backend.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> readAll() {  //für den Admin
        return notificationService.readAll();
    }

    @GetMapping("/{id}")
    public Notification read(@PathVariable UUID id) {
        return notificationService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestBody @Valid Notification notification) {
        return notificationService.create(notification);
    }

    @PutMapping
    public Notification update(@RequestBody @Valid Notification notification) {
        return notificationService.update(notification);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        notificationService.delete(id);
    }
}