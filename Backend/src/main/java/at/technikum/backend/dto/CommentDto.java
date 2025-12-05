package at.technikum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private UUID id;

    @NotNull(message = "Article ID is required")
    private UUID articleId;

    private UUID userId;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @NotBlank(message = "Text is required")
    @Size(max = 5000, message = "Text must be less than 5000 characters")
    private String text;

    private LocalDateTime createdAt;
}
