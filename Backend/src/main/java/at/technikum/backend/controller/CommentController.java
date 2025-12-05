package at.technikum.backend.controller;

import at.technikum.backend.dto.CommentDto;
import at.technikum.backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getComments(
            @RequestParam(required = false) UUID articleId) {
        if (articleId != null) {
            return commentService.findByArticleId(articleId);
        }
        return commentService.findAll();
    }

    @GetMapping("/my-comments")
    public List<CommentDto> findMyComments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.findByUserEmail(userDetails.getUsername());
    }

    @GetMapping("/user/{userId}")
    public List<CommentDto> findByUserId(
            @PathVariable UUID userId) {
        return commentService.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(
            @RequestBody @Valid CommentDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.create(dto, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(
            @PathVariable UUID id,
            @RequestBody @Valid CommentDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.update(id, dto, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.delete(id, userDetails.getUsername());
    }
}