package at.technikum.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
public class RegisteredUser {

    @Id
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private char sex;   //char: w,m,d keine anderen werden akzeptiert
    private String land;
    private boolean isAdmin;

    public RegisteredUser(String username, String email, String password, char sex, String land) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.land = land;

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

    public String getLand() {
        return this.land;
    }

    public void setLand() {
        this.land = land;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }


}