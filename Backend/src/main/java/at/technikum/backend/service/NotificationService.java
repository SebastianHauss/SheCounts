package at.technikum.backend.service;

import at.technikum.backend.entity.Notification;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityIdDoesNotMatchException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> readAll() {
        return notificationRepository.findAll();
    }

    public Notification read(UUID id) {
        if (checkIfExistsById(id).isEmpty()) {
            throw new EntityNotFoundException("Notification not found.");
        }
        return checkIfExistsById(id).get();
    }

    public Notification create(Notification notification) {
        if (notification.getId() != null && checkIfExistsById(notification.getId()).isPresent()) {
            throw new EntityAlreadyExistsException("Notification already exists.");
        }
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification update(UUID id, Notification notification) {

        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found."));

        // Felder aktualisieren
        existing.setTitle(notification.getTitle());
        existing.setMessage(notification.getMessage());
        existing.setRead(notification.isRead());
        existing.setUser(notification.getUser());

        return notificationRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        if (checkIfExistsById(id).isEmpty()) {
            throw new EntityNotFoundException("Notification not found.");
        }
        notificationRepository.delete(checkIfExistsById(id).get());
    }

    public Optional<Notification> checkIfExistsById(UUID id) {
        return notificationRepository.findById(id);
    }
}
