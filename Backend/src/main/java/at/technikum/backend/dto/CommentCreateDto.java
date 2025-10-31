package at.technikum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    private UUID articleId;
    private UUID userId;
    private String title;
    private String text;
    private UUID commentId;

}