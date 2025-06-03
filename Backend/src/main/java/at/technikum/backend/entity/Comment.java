package at.technikum.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {
    /*
    Todo: verschachtelte Comments recherche
    Todo: Foreign Key Article_Id
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private String author;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }


    public Comment(Article article_id, String content, String author) {
        this.article = article_id;
        this.content = content;
        this.author = author;

        this.createdAt = LocalDateTime.now();
    }

    public Comment() {
    }

    // GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public Article getArticle_id() {
        return this.article;
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
