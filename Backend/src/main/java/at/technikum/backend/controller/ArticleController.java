package at.technikum.backend.controller;

import at.technikum.backend.entity.Article;
import at.technikum.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ArticleController {


    @Autowired
    public ArticleController() {
    }


    @GetMapping("article")
    public List<Article> readAll() {
        return null;
    }

    @GetMapping("/{id}")
    public Article read(@PathVariable String  id) {
        return null;
    }

    @PostMapping
    public Article create(@RequestBody Article article) {
        return article;
    }

    @PutMapping
    public Article update(@RequestBody Article article) {
       return article;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        ;
    }


}
