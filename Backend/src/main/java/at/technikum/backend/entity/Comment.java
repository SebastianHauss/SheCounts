package at.technikum.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {
    /*
    Todo: verschachtelte Comments recherche
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@ForeignKey(Article)
    private String article_id;
    private String content;
    private String author;
    private LocalDateTime createdAt;


    public Comment(String article_id, String content, String author){
        this.article_id = article_id;
        this.content = content;
        this.author = author;

        this.createdAt = LocalDateTime.now();
    }

    public Comment() {}

    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
