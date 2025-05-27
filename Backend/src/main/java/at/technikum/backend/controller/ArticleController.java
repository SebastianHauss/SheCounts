package at.technikum.backend.controller;

import at.technikum.backend.entity.Article;
import at.technikum.backend.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("article")
    public List<Article> readAll() {
        return articleService.readAll();
    }

    @GetMapping("/{id}")
    public Article read(@PathVariable String id) {
        return articleService.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Article create(@RequestBody Article article) {
        return articleService.create(article);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Article update(@RequestBody Article article) {
        return articleService.update(article);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        articleService.delete(id);
    }

}
