package at.technikum.backend.service;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.exceptions.*;
import at.technikum.backend.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
