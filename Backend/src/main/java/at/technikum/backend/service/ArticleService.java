package at.technikum.backend.service;

import at.technikum.backend.entity.Article;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityIdDoesNotMatchException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        Article article = checkIfArticleExists(id)
                .orElseThrow(() -> new EntityNotFoundException("Article couldn't be found."));

        // Load markdown file content from resources
        String filename = article.getContent();

        try {
            ClassPathResource resource = new ClassPathResource(filename);
            String fileContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            article.setContent(fileContent);
        } catch (IOException e) {
            throw new RuntimeException("Could not load article content file: " + filename, e);
        }

        return article;
    }

    public Article create(Article article) {
        if (article.getId() != null && checkIfArticleExists(article.getId()).isPresent()) {
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

    public Optional<Article> checkIfArticleExists(UUID id) {
        return articleRepository.findById(id);
    }
}