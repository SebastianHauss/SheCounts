package at.technikum.backend.controller;

import at.technikum.backend.dto.NotificationDto;
import at.technikum.backend.entity.Notification;
import at.technikum.backend.mapper.NotificationMapper;
import at.technikum.backend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public List<NotificationDto> readAll() {  //für den Admin
        return notificationService.readAll().stream().map(notificationMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public NotificationDto read(@PathVariable UUID id) {
        return notificationMapper.toDto(notificationService.read(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationDto create(@RequestBody @Valid Notification notification) {
        return notificationMapper.toDto(notificationService.create(notification));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationDto update(@PathVariable UUID id, @RequestBody @Valid NotificationDto notificationDto) {
        return notificationMapper.toDto(
                notificationService.update(id, notificationMapper.toEntity(notificationDto))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        notificationService.delete(id);
    }
}