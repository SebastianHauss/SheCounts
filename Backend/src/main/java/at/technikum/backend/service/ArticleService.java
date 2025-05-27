package at.technikum.backend.service;

import at.technikum.backend.entity.Article;
import at.technikum.backend.exceptions.ArticleAlreadyExistsException;
import at.technikum.backend.exceptions.ArticleNotFoundException;
import at.technikum.backend.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public Article create(Article article) {
        if (checkIfArticleExists(article.getId()).isPresent()) {
            throw new ArticleAlreadyExistsException("Article already exists.");
        }
        return articleRepository.save(article);
    }

    public List<Article> readAll() {
        return articleRepository.findAll();
    }

    public Article read(String id) {
        if (checkIfArticleExists(id).isEmpty()) {
            throw new ArticleNotFoundException("Article couldn't be found.");
        }
        return checkIfArticleExists(id).get();
    }

    public Article update(Article article) {
        if (checkIfArticleExists(article.getId()).isEmpty()) {
            throw new ArticleNotFoundException("Article couldn't be found.");
        }
        return articleRepository.save(article);
    }

    public void delete(String id) {
        if (checkIfArticleExists(id).isEmpty()) {
            throw new ArticleNotFoundException("Article couldn't be found.");
        }
        articleRepository.delete(checkIfArticleExists(id).get());
    }

    public Optional<Article> checkIfArticleExists(String id) {
        return articleRepository.findById(id);
    }

}
