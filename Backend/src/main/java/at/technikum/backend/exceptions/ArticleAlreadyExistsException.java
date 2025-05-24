package at.technikum.backend.exceptions;

public class ArticleAlreadyExistsException extends RuntimeException {
    public ArticleAlreadyExistsException(String message) {
        super(message);
    }
}
