package at.technikum.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NotificationAlreadyExistsException extends RuntimeException {
    public NotificationAlreadyExistsException(String message) {
        super(message);
    }
}
