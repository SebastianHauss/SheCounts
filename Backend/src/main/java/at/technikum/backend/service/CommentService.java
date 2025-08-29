package at.technikum.backend.service;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.entity.Article;
import at.technikum.backend.entity.User;
import at.technikum.backend.exceptions.*;
import at.technikum.backend.repository.ArticleRepository;
import at.technikum.backend.repository.CommentRepository;
import at.technikum.backend.dto.CommentCreateDto;
import at.technikum.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          ArticleRepository articleRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<Comment> readAll() {
        return commentRepository.findAll();
    }

    public Comment read(UUID id) {
        if (checkIfCommentExistsById(id).isEmpty()) {
            throw new EntityNotFoundException("Comment not found.");
        }
        return checkIfCommentExistsById(id).get();
    }

    public Comment create(Comment comment) {
        if (checkIfCommentExistsById(comment.getId()).isPresent()) {
            throw new EntityAlreadyExistsException("Comment already exists.");
        }
        return commentRepository.save(comment);
    }

    public Comment create(CommentCreateDto dto) {
        Article article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = new Comment(article, user, dto.getTitle(), dto.getText());
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment update(UUID id, Comment comment) {
        if (checkIfCommentExistsById(comment.getId()).isEmpty()) {
            throw new EntityNotFoundException("Comment not found.");
        }
        if (!id.equals(comment.getId())) {
            throw new EntityIdDoesNotMatchException("UUID doesn't match Object Id");
        }
        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(UUID id) {
        if (checkIfCommentExistsById(id).isEmpty()) {
            throw new EntityNotFoundException("Comment not found.");
        }
        commentRepository.delete(checkIfCommentExistsById(id).get());
    }

    public Optional<Comment> checkIfCommentExistsById(UUID id) {
        return commentRepository.findById(id);
    }
}
