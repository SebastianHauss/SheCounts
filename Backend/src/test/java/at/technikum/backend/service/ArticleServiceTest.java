package at.technikum.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import at.technikum.backend.entity.Article;
import at.technikum.backend.exceptions.EntityAlreadyExistsException;
import at.technikum.backend.exceptions.EntityIdDoesNotMatchException;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.repository.ArticleRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArticleService Unit Tests")
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    // ====================================================================
    // POSITIVE FÄLLE
    // ====================================================================

    @Test
    void readAll_shouldReturnAllArticles() {
        // ---------- given ----------
        Article article1 = new Article("Content 1", "Author 1");
        Article article2 = new Article("Content 2", "Author 2");

        when(articleRepository.findAll())
                .thenReturn(List.of(article1, article2));

        // ---------- when ----------
        List<Article> result = articleService.readAll();

        // ---------- then ----------
        assertEquals(2, result.size());
        verify(articleRepository).findAll();  // 1 call
    }

    @Test
    void read_shouldReturnArticle_whenExists() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.of(article));

        // ---------- when ----------
        Article result = articleService.read(articleId);

        // ---------- then ----------
        assertNotNull(result);
        assertSame(article, result);
        verify(articleRepository, times(2)).findById(articleId);  // 2 calls!
    }

    @Test
    void create_shouldSaveArticle_whenArticleHasNoId() {
        // ---------- given ----------
        Article article = new Article("Content", "Author");
        // article.getId() ist null
        Article savedArticle = new Article("Content", "Author");

        when(articleRepository.save(article))
                .thenReturn(savedArticle);

        // ---------- when ----------
        Article result = articleService.create(article);

        // ---------- then ----------
        assertSame(savedArticle, result);
        verify(articleRepository).save(article);
        verify(articleRepository, never()).findById(any());  // 0 calls because id is null
    }

    @Test
    void create_shouldSaveArticle_whenArticleIdDoesNotExist() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.empty());
        when(articleRepository.save(article))
                .thenReturn(article);

        // ---------- when ----------
        Article result = articleService.create(article);

        // ---------- then ----------
        assertSame(article, result);
        verify(articleRepository).findById(articleId);  // 1 call
        verify(articleRepository).save(article);
    }

    @Test
    void update_shouldUpdateArticle_whenIdsMatch() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("Updated Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.of(article));
        when(articleRepository.save(article))
                .thenReturn(article);

        // ---------- when ----------
        Article result = articleService.update(articleId, article);

        // ---------- then ----------
        assertSame(article, result);
        verify(articleRepository).findById(articleId);  // 1 call
        verify(articleRepository).save(article);
    }

    @Test
    void delete_shouldDeleteArticle_whenExists() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.of(article));

        // ---------- when ----------
        articleService.delete(articleId);

        // ---------- then ----------
        verify(articleRepository, times(2)).findById(articleId);  // 2 calls!
        verify(articleRepository).delete(article);
    }

    @Test
    void checkIfArticleExists_shouldReturnArticle_whenFound() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.of(article));

        // ---------- when ----------
        Optional<Article> result = articleService.checkIfArticleExists(articleId);

        // ---------- then ----------
        assertEquals(Optional.of(article), result);
        verify(articleRepository).findById(articleId);  // 1 call
    }

    // ====================================================================
    // NEGATIVE FÄLLE
    // ====================================================================

    @Test
    void read_shouldThrowException_whenArticleNotFound() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.empty());

        // ---------- when + then ----------
        assertThrows(EntityNotFoundException.class,
                () -> articleService.read(articleId));

        verify(articleRepository).findById(articleId);  // 1 call (throws before 2nd)
    }

    @Test
    void create_shouldThrowException_whenArticleAlreadyExists() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("New Content", "Author");
        article.setId(articleId);

        Article existingArticle = new Article("Existing", "Author");
        existingArticle.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.of(existingArticle));

        // ---------- when + then ----------
        assertThrows(EntityAlreadyExistsException.class,
                () -> articleService.create(article));

        verify(articleRepository).findById(articleId);  // 1 call
        verify(articleRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenArticleNotFound() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Article article = new Article("Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.empty());

        // ---------- when + then ----------
        assertThrows(EntityNotFoundException.class,
                () -> articleService.update(articleId, article));

        verify(articleRepository).findById(articleId);  // 1 call
        verify(articleRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenIdDoesNotMatch() {
        // ---------- given ----------
        UUID pathId = UUID.randomUUID();
        UUID articleId = UUID.randomUUID();

        Article article = new Article("Content", "Author");
        article.setId(articleId);

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.of(article));

        // ---------- when + then ----------
        assertThrows(EntityIdDoesNotMatchException.class,
                () -> articleService.update(pathId, article));

        verify(articleRepository).findById(articleId);  // 1 call
        verify(articleRepository, never()).save(any());
    }

    @Test
    void delete_shouldThrowException_whenArticleNotFound() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.empty());

        // ---------- when + then ----------
        assertThrows(EntityNotFoundException.class,
                () -> articleService.delete(articleId));

        verify(articleRepository).findById(articleId);  // 1 call (throws before 2nd)
        verify(articleRepository, never()).delete(any());
    }

    @Test
    void checkIfArticleExists_shouldReturnEmpty_whenNotFound() {
        // ---------- given ----------
        UUID articleId = UUID.randomUUID();

        when(articleRepository.findById(articleId))
                .thenReturn(Optional.empty());

        // ---------- when ----------
        Optional<Article> result = articleService.checkIfArticleExists(articleId);

        // ---------- then ----------
        assertEquals(Optional.empty(), result);
        verify(articleRepository).findById(articleId);  // 1 call
    }
}