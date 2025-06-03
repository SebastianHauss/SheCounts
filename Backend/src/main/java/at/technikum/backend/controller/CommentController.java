package at.technikum.backend.controller;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment create(@RequestBody Comment comment) {
        return null;
    }

    @GetMapping("/")
    public Comment readAll() {
        return null;
    }

    @GetMapping("/{id}")
    public Comment read(@PathVariable int id) {
        return null;
    }

    @PutMapping
    public Comment update(@RequestBody Comment comment) {
        return null;
    }

    @DeleteMapping("/{id]")
    public void delete(@PathVariable int id) {

    }
}
