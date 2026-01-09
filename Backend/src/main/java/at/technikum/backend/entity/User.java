package at.technikum.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "app_user")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "username darf nicht leer sein")
    private String username;

    @Email
    @Column(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean isAdmin = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @Column(name = "profile_picture_id")
    private String profilePictureId;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

        this.notifications = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public User() {
        this.notifications = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}