package agrechnev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Oleksiy Grechnyev on 12/18/2016.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDtoCreateException extends RuntimeException {
    public InvalidDtoCreateException(String message) {
        super(message);
    }

    public InvalidDtoCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
