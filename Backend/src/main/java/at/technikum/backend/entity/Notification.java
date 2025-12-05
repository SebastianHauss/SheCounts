package at.technikum.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    private boolean read = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    private User user;

    public Notification() {}

    public Notification(User user, String title, String message) {
        this.user = user;
        this.title = title;
        this.message = message;
    }
}