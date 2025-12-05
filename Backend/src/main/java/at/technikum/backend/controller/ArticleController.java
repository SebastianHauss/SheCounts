package at.technikum.backend.controller;

import at.technikum.backend.dto.ArticleDto;
import at.technikum.backend.entity.Article;
import at.technikum.backend.mapper.ArticleMapper;
import at.technikum.backend.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleMapper articleMapper;

    /*
    @GetMapping("/content/{filename}/html")
    public String getArticleHtml(@PathVariable String filename) {
        return articleService.getArticleContentByFilename(filename);
    }
    */
    
    @GetMapping
    public List<ArticleDto> readAll() {
        List<Article> articleList = articleService.readAll();
        return articleList.stream()
                .map(articleMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ArticleDto read(@PathVariable UUID id) {
        return articleMapper.toDto(articleService.read(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto create(@RequestBody @Valid Article article) {
        return articleMapper.toDto(articleService.create(article));
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