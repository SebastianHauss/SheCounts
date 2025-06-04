package at.technikum.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityIdDoesNotMatchException extends RuntimeException {
    public EntityIdDoesNotMatchException(String message) {
        super(message);
    }
}
