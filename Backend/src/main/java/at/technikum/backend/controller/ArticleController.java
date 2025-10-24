package at.technikum.backend.controller;

import at.technikum.backend.entity.Article;
import at.technikum.backend.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping("/api/articles")
public class ArticleController { /*fjjkfjf*/

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("/content/{filename}/html")
    public String getArticleHtml(@PathVariable String filename) {
        return articleService.getArticleContentByFilename(filename);
    }

    @GetMapping
    public List<Article> readAll() {
        return articleService.readAll();
    }

    @GetMapping("/{id}")
    public Article read(@PathVariable UUID id) {
        return articleService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Article create(@RequestBody @Valid Article article) {
        return articleService.create(article);
    }

    @PutMapping("/{id}")
    public Article update(@PathVariable UUID id, @RequestBody @Valid Article article) {
        return articleService.update(id, article);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        articleService.delete(id);
    }
}