package at.technikum.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ArticleDto {
    private UUID id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private List<UUID> commentsIds;
}
