package at.technikum.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String content;

    @NotBlank
    private String author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Article(String content, String author) {
        this.content = content;
        this.author = author;
        this.comments = new ArrayList<>();
    }

    public Article() {
        this.comments = new ArrayList<>();
    }

}