package at.technikum.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.UUID;

@Entity
public class RegisteredUser {

    @Id
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    @UniqueElements
    private String email;
    @NotBlank
    private String password;
    private char sex;   // w,m,d
    private String country;
    private boolean isAdmin;

    // TODO: Validation
    public RegisteredUser(
            String username,
            String email,
            String password,
            char sex,
            String country
    ) {
        this.username = username;
        // TODO: @Email @NotBlank
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.country = country;

        this.id = UUID.randomUUID().toString();
        this.isAdmin = false;
    }

    public RegisteredUser() {
        this.id = UUID.randomUUID().toString();
        this.isAdmin = false;
    }

    //GETTERS AND SETTERS
    public String getId() {
        return id;
    }

    public char getSex() {
        return this.sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
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

    public String getCountry() {
        return this.country;
    }

    public void setCountry() {
        this.country = country;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


}