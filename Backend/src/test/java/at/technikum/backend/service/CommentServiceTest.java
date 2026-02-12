package at.technikum.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

import at.technikum.backend.dto.CommentDto;
import at.technikum.backend.entity.Article;
import at.technikum.backend.entity.Comment;
import at.technikum.backend.entity.User;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.exceptions.UnauthorizedException;
import at.technikum.backend.mapper.CommentMapper;
import at.technikum.backend.repository.ArticleRepository;
import at.technikum.backend.repository.CommentRepository;
import at.technikum.backend.repository.UserRepository;

// Bindet Mockito an JUnit 5, damit @Mock und @InjectMocks funktionieren
@ExtendWith(MockitoExtension.class)
@DisplayName("CommentService Unit Tests")
public class CommentServiceTest {

    // Mock für das CommentRepository
    // Simuliert Datenbankzugriffe ohne echte DB
    @Mock
    private CommentRepository commentRepository;

    // Mock für das ArticleRepository
    @Mock
    private ArticleRepository articleRepository;

    // Mock für das UserRepository
    @Mock
    private UserRepository userRepository;

    // Mock für den Mapper (Entity <-> DTO)
    // Mapper-Logik wird hier nicht getestet
    @Mock
    private CommentMapper mapper;

    // Erstellt eine echte Instanz von CommentService
    // und injiziert alle @Mock Dependencies automatisch
    @InjectMocks
    private CommentService commentService;

    // ====================================================================
    // POSITIVE FÄLLE
    // ====================================================================

    // Testet den Happy Path:
    // - User existiert
    // - Kommentare existieren
    // - DTOs werden korrekt zurückgegeben
    @Test
    void findByUserEmail_shouldReturnComments() {

        // ---------- given ----------
        // Testdaten vorbereiten

        String email = "test@example.com";
        UUID userId = UUID.randomUUID();

        // Simulierter User aus dem Repository
        User user = new User();
        user.setId(userId);

        // Simulierter Kommentar aus dem Repository
        Comment comment = new Comment();

        // Simuliertes DTO aus dem Mapper
        CommentDto dto = new CommentDto();

        // Repository gibt einen User zurück
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        // Repository gibt eine Kommentar-Liste zurück
        when(commentRepository.findByUserId(userId))
                .thenReturn(List.of(comment));

        // Mapper wandelt Comment in CommentDto um
        when(mapper.toDto(comment))
                .thenReturn(dto);

        // ---------- when ----------
        // Methode unter Test aufrufen
        List<CommentDto> result = commentService.findByUserEmail(email);

        // ---------- then ----------
        // Ergebnis überprüfen

        // Erwartet genau ein DTO
        assertEquals(1, result.size());

        // Erwartet genau das gemockte DTO
        assertSame(dto, result.get(0));

        // Verifiziert, dass die Repositories korrekt aufgerufen wurden
        verify(userRepository).findByEmail(email);
        verify(commentRepository).findByUserId(userId);
    }

    @Test
    void findAll_shouldReturnComments() {

        // ---------- given ----------
        Comment comment = new Comment();
        CommentDto dto = new CommentDto();

        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(mapper.toDto(comment)).thenReturn(dto);

        // ---------- when ----------
        List<CommentDto> result = commentService.findAll();

        // ---------- then ----------
        assertEquals(1, result.size());
        verify(commentRepository).findAll();
    }

    @Test
    void findByArticleId_shouldReturnComments() {

        // ---------- given ----------
        UUID articleId = UUID.randomUUID();
        Comment comment = new Comment();
        CommentDto dto = new CommentDto();

        when(commentRepository.findByArticleId(articleId))
                .thenReturn(List.of(comment));
        when(mapper.toDto(comment))
                .thenReturn(dto);

        // ---------- when ----------
        List<CommentDto> result = commentService.findByArticleId(articleId);

        // ---------- then ----------
        assertEquals(1, result.size());
    }

    @Test
    void findByUserId_shouldReturnComments() {

        // ---------- given ----------
        UUID userId = UUID.randomUUID();
        Comment comment = new Comment();
        CommentDto dto = new CommentDto();

        when(commentRepository.findByUserId(userId))
                .thenReturn(List.of(comment));
        when(mapper.toDto(comment))
                .thenReturn(dto);

        // ---------- when ----------
        List<CommentDto> result = commentService.findByUserId(userId);

        // ---------- then ----------
        assertEquals(1, result.size());
    }

    @Test
    void create_shouldSaveComment() {

        // ---------- given ----------
        String email = "user@example.com";
        UUID articleId = UUID.randomUUID();

        CommentDto dto = new CommentDto();
        dto.setArticleId(articleId);

        Comment comment = new Comment();
        Comment saved = new Comment();
        CommentDto resultDto = new CommentDto();

        Article article = new Article();
        User user = new User();

        when(mapper.toEntity(dto)).thenReturn(comment);
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(commentRepository.save(comment)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(resultDto);

        // ---------- when ----------
        CommentDto result = commentService.create(dto, email);

        // ---------- then ----------
        assertSame(resultDto, result);
    }

    @Test
    void update_shouldUpdateComment_whenOwner() {

        // ---------- given ----------
        UUID commentId = UUID.randomUUID();
        String email = "user@example.com";

        User user = new User();
        user.setId(UUID.randomUUID());

        Comment existing = new Comment();
        existing.setUser(user);

        CommentDto dto = new CommentDto();
        dto.setTitle("new");
        dto.setText("text");

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(existing));
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        when(commentRepository.save(existing))
                .thenReturn(existing);
        when(mapper.toDto(existing))
                .thenReturn(dto);

        // ---------- when ----------
        CommentDto result = commentService.update(commentId, dto, email);

        // ---------- then ----------
        assertSame(dto, result);
    }

    @Test
    void delete_shouldDeleteComment_whenAdmin() {

        // ---------- given ----------
        UUID commentId = UUID.randomUUID();
        String email = "admin@example.com";

        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setAdmin(true);

        User owner = new User();
        owner.setId(UUID.randomUUID());

        Comment comment = new Comment();
        comment.setUser(owner);

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        // ---------- when ----------
        commentService.delete(commentId, email);

        // ---------- then ----------
        verify(commentRepository).delete(comment);
    }

    // ====================================================================
    // NEGATIVE FÄLLE
    // ====================================================================

    // Testet den Fehlerfall:
    // - User existiert nicht
    // - Exception wird geworfen
    // - Keine weiteren Repository-Aufrufe passieren
    @Test
    void findByUserEmail_shouldThrowException_whenUserNotFound() {

        // ---------- given ----------
        String email = "missing@example.com";

        // Repository findet keinen User
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // ---------- when + then ----------
        // Erwartet eine EntityNotFoundException
        assertThrows(EntityNotFoundException.class,
                () -> commentService.findByUserEmail(email));

        // Sicherstellen, dass keine Kommentare geladen wurden
        verify(commentRepository, never()).findByUserId(any());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoCommentsExist() {

        // ---------- given ----------
        when(commentRepository.findAll()).thenReturn(List.of());

        // ---------- when ----------
        List<CommentDto> result = commentService.findAll();

        // ---------- then ----------
        assertEquals(0, result.size());
    }

    @Test
    void findByArticleId_shouldReturnEmptyList_whenNoCommentsExist() {

        // ---------- given ----------
        UUID articleId = UUID.randomUUID();

        when(commentRepository.findByArticleId(articleId))
                .thenReturn(List.of());

        // ---------- when ----------
        List<CommentDto> result = commentService.findByArticleId(articleId);

        // ---------- then ----------
        assertEquals(0, result.size());
    }

    @Test
    void findByUserId_shouldReturnEmptyList_whenNoCommentsExist() {

        // ---------- given ----------
        UUID userId = UUID.randomUUID();

        when(commentRepository.findByUserId(userId))
                .thenReturn(List.of());

        // ---------- when ----------
        List<CommentDto> result = commentService.findByUserId(userId);

        // ---------- then ----------
        assertEquals(0, result.size());
    }

    @Test
    void create_shouldThrowException_whenArticleNotFound() {

        // ---------- given ----------
        CommentDto dto = new CommentDto();
        dto.setArticleId(UUID.randomUUID());

        when(mapper.toEntity(dto)).thenReturn(new Comment());
        when(articleRepository.findById(any()))
                .thenReturn(Optional.empty());

        // ---------- when + then ----------
        assertThrows(EntityNotFoundException.class,
                () -> commentService.create(dto, "user@example.com"));
    }

    // Testet die Security-Logik im Update:
    // - User ist nicht der Besitzer
    // - User ist kein Admin
    // - Update ist nicht erlaubt
    @Test
    void update_shouldThrowUnauthorized_whenNotOwnerAndNotAdmin() {

        // ---------- given ----------
        UUID commentId = UUID.randomUUID();
        String email = "user@example.com";

        // Besitzer des Kommentars
        User owner = new User();
        owner.setId(UUID.randomUUID());

        // Anfragender User (nicht Besitzer, kein Admin)
        User requester = new User();
        requester.setId(UUID.randomUUID());
        requester.setAdmin(false);

        // Kommentar gehört einem anderen User
        Comment comment = new Comment();
        comment.setUser(owner);

        // Kommentar wird gefunden
        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));

        // Anfragender User wird gefunden
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(requester));

        // ---------- when + then ----------
        // Erwartet UnauthorizedException
        assertThrows(UnauthorizedException.class,
                () -> commentService.update(commentId, new CommentDto(), email));

        // Sicherstellen, dass kein Save passiert
        verify(commentRepository, never()).save(any());
    }

    @Test
    void delete_shouldThrowUnauthorized_whenNotOwnerAndNotAdmin() {

        // ---------- given ----------
        UUID commentId = UUID.randomUUID();

        User owner = new User();
        owner.setId(UUID.randomUUID());

        User requester = new User();
        requester.setId(UUID.randomUUID());
        requester.setAdmin(false);

        Comment comment = new Comment();
        comment.setUser(owner);

        when(commentRepository.findById(commentId))
                .thenReturn(Optional.of(comment));
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(requester));

        // ---------- when + then ----------
        assertThrows(UnauthorizedException.class,
                () -> commentService.delete(commentId, "user@example.com"));
    }

}
