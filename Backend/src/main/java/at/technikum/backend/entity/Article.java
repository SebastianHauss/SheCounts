package at.technikum.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Article {

    @Id
    private String id;
    private String content;
    private LocalDateTime dateOfCreation;


    public Article(String content) {
        this.content = content;
        this.dateOfCreation = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
    }

    public Article() {
        this.id = UUID.randomUUID().toString();
    }


    //GETTERS AND SETTERS   (no option to set Id)
    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getDateOfCreation() {
        return this.dateOfCreation;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDateOfCreation() {
        this.dateOfCreation = dateOfCreation;
    }


}
