package at.technikum.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "RegisteredUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String username;

    @NotBlank
    @UniqueElements
    private String email;

    @NotBlank
    private String password;

    private boolean isAdmin = false;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new LinkedList<>();

    @OneToOne(mappedBy = "user")
    private Profile profile;

    // TODO: Validation
    public User(
            String username,
            String email,
            String password,
            char sex,
            String country
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

    //GETTERS AND SETTERS
    public String getId() {
        return id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername() {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail() {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword() {
        this.password = password;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

}