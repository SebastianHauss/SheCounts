package at.technikum.backend.service;

import at.technikum.backend.entity.Article;
import at.technikum.backend.exceptions.ArticleAlreadyExistsException;
import at.technikum.backend.exceptions.ArticleNotFoundException;
import at.technikum.backend.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
            throw new ArticleNotFoundException("Article couldn't be found.");
        }
        return checkIfArticleExists(id).get();
    }

    public Article create(Article article) {
        if (checkIfArticleExists(article.getId()).isPresent()) {
            throw new ArticleAlreadyExistsException("Article already exists.");
        }
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(UUID id, Article article) {
        if (checkIfArticleExists(article.getId()).isEmpty()) {
            throw new ArticleNotFoundException("Article couldn't be found.");
        }
        if (id != article.getId()) {
            throw new ArticleNotFoundException("ID does not match any article id.");
        }
        return articleRepository.save(article);
    }

    @Transactional
    public void delete(UUID id) {
        if (checkIfArticleExists(id).isEmpty()) {
            throw new ArticleNotFoundException("Article couldn't be found.");
        }
        articleRepository.delete(checkIfArticleExists(id).get());
    }

    public Optional<Article> checkIfArticleExists(UUID id) {
        return articleRepository.findById(id);
    }
}