package at.technikum.backend.service;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.exceptions.CommentAlreadyExistsException;
import at.technikum.backend.exceptions.CommentNotFoundException;
import at.technikum.backend.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment create(Comment comment) {
        if (checkIfCommentExistsById(comment.getId()).isPresent()) {
            throw new CommentAlreadyExistsException("Comment already exists.");
        }
        return commentRepository.save(comment);
    }

    public List<Comment> readAll() {
        return commentRepository.findAll();
    }

    public Comment read(int id) {
        if (checkIfCommentExistsById(id).isEmpty()) {
            throw new CommentNotFoundException("Comment not found.");
        }
        return checkIfCommentExistsById(id).get();
    }

    @Transactional
    public Comment update(Comment comment) {
        if (checkIfCommentExistsById(comment.getId()).isEmpty()) {
            throw new CommentNotFoundException("Comment not found.");
        }
        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(int id) {
        if (checkIfCommentExistsById(id).isEmpty()) {
            throw new CommentNotFoundException("Comment not found.");
        }
        commentRepository.delete(checkIfCommentExistsById(id).get());
    }
    
    public Optional<Comment> checkIfCommentExistsById(int id) {
        return commentRepository.findById(id);
    }
}
