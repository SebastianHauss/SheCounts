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
    private String author;


    public Article(String content, String author) {
        this.content = content;
        this.author = author;

        this.id = UUID.randomUUID().toString();
        this.dateOfCreation = LocalDateTime.now();
    }

    public Article() {
        this.id = UUID.randomUUID().toString();
    }


    //GETTERS AND SETTERS
    public String getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateOfCreation() {
        return this.dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}