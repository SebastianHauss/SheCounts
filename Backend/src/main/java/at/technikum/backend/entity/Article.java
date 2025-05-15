package at.technikum.backend.entity;

import java.time.LocalDateTime;

public class Article {

    private int id;
    private String article;
    private LocalDateTime dateOfCreation;


    public Article(int id, String article, LocalDateTime dateOfCreation) {
        this.id = id;
        this.article = article;
        this.dateOfCreation = dateOfCreation;
    }


    //GETTERS AND SETTERS   (no option to set Id)
    public int getId() {
        return this.id;
    }

    public String getArticle() {
        return this.article;
    }

    public LocalDateTime getDateOfCreation() {
        return this.dateOfCreation;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setDateOfCreation() {
        this.dateOfCreation = dateOfCreation;
    }


}
