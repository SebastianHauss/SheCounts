package at.technikum.backend.controller;

import at.technikum.backend.entity.Article;
import at.technikum.backend.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Article create(@RequestBody @Valid Article article) {
        return articleService.create(article);
    }

    @GetMapping("/")
    public List<Article> readAll() {
        return articleService.readAll();
    }

    @GetMapping("/{id}")
    public Article read(@PathVariable UUID id) {
        return articleService.read(id);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Article update(@RequestBody Article article) {
        return articleService.update(article);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        articleService.delete(id);
    }
}
