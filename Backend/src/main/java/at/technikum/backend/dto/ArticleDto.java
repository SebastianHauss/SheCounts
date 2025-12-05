package at.technikum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private UUID id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
