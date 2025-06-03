package at.technikum.backend.controller;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody @Valid Comment comment) {
        return commentService.create(comment);
    }

    @GetMapping("/")
    public List<Comment> readAll() {
        return commentService.readAll();
    }

    @GetMapping("/{id}")
    public Comment read(@PathVariable int id) {
        return commentService.read(id);
    }

    @PutMapping
    public Comment update(@RequestBody Comment comment) {
        return commentService.update(comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        commentService.delete(id);
    }
}
