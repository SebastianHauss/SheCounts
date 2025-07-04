package at.technikum.backend.service;

import at.technikum.backend.entity.Article;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityIdDoesNotMatchException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.ArticleRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    
    public List<Article> readAll() {
        return articleRepository.findAll();
    }

    public Article read(UUID id) {
        if (checkIfArticleExists(id).isEmpty()) {
            throw new EntityNotFoundException("Article couldn't be found.");
        }
        return checkIfArticleExists(id).get();
    }

    public Article create(Article article) {
        if (checkIfArticleExists(article.getId()).isPresent()) {
            throw new EntityAlreadyExistsException("Article already exists.");
        }
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(UUID id, Article article) {
        if (checkIfArticleExists(article.getId()).isEmpty()) {
            throw new EntityNotFoundException("Article couldn't be found.");
        }
        if (!id.equals(article.getId())) {
            throw new EntityIdDoesNotMatchException("UUID doesn't match Object Id");
        }
        return articleRepository.save(article);
    }

    @Transactional
    public void delete(UUID id) {
        if (checkIfArticleExists(id).isEmpty()) {
            throw new EntityNotFoundException("Article couldn't be found.");
        }
        articleRepository.delete(checkIfArticleExists(id).get());
    }

    public String getArticleContentByFilename(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("articles/" + filename);
            return Files.readString(resource.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Could not read article content from file: " + filename, e);
        }
    }





    public Optional<Article> checkIfArticleExists(UUID id) {
        return articleRepository.findById(id);
    }
}