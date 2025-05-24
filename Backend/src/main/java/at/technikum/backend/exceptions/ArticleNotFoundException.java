package at.technikum.backend.exceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleNotFoundException(Throwable cause) {
        super(cause);
    }

    public ArticleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ArticleNotFoundException() {
    }
}
