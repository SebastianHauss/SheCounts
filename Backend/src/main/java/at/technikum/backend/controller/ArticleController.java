package at.technikum.backend.controller;

import at.technikum.backend.entity.Article;
import at.technikum.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ArticleController {

    private ArticleService articleService = new ArticleService();

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("article")
    public List<Article> readAll() {
        return articleService.getArticles();
    }

    @GetMapping("/{id}")
    public Article read(@PathVariable int id) {
        return articleService.getArticle(id);
    }

    @PostMapping
    public void addArticle(@RequestBody Article article) {
        articleService.addArticle(article);
    }

    @PutMapping
    public void updateArticle(@RequestBody Article article, @RequestBody String newText) {
        articleService.updateArticle(article, newText);
    }

    @DeleteMapping
    public void deleteArticle(@RequestBody Article article) {
        articleService.deleteArticle(article);
    }


}
