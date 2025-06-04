package at.technikum.backend.controller;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<Comment> readAll() {
        return commentService.readAll();
    }

    @GetMapping("/{id}")
    public Comment read(@PathVariable UUID id) {
        return commentService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody @Valid Comment comment) {
        return commentService.create(comment);
    }

    @PutMapping
    public Comment update(@RequestBody @Valid Comment comment) {
        return commentService.update(comment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        commentService.delete(id);
    }
}