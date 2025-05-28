package at.technikum.backend.service;

import at.technikum.backend.entity.Notification;
import at.technikum.backend.exceptions.NotificationAlreadyExistsException;
import at.technikum.backend.exceptions.NotificationNotFoundException;
import at.technikum.backend.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public Notification create(Notification notification){
        if(checkIfExistsById(notification.getId()).isPresent()){
            throw new NotificationAlreadyExistsException("Notification already exists.");
        }
        return notificationRepository.save(notification);
    }

    public List<Notification> readAll(){
        return notificationRepository.findAll();
    }

    public Notification read(String id){
        if (checkIfExistsById(id).isEmpty()){
            throw new NotificationNotFoundException("Notification not found.");
        }
        return checkIfExistsById(id).get();
    }

    @Transactional
    public Notification update(Notification notification){
        if(checkIfExistsById(notification.getId()).isEmpty()){
            throw new NotificationNotFoundException("Notification not found.");
        }
        return notificationRepository.save(notification);
    }

    @Transactional
    public void delete(String id){
        if(checkIfExistsById(id).isEmpty()){
            throw new NotificationNotFoundException("Notification not found.");
        }
        notificationRepository.delete(checkIfExistsById(id).get());
    }


    public Optional<Notification> checkIfExistsById(String id){
        return notificationRepository.findById(id);
    }

}
