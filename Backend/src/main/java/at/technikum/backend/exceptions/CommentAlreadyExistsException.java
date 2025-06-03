package at.technikum.backend.exceptions;

public class CommentAlreadyExistsException extends RuntimeException {
    public CommentAlreadyExistsException(String message) {
        super(message);
    }
}
