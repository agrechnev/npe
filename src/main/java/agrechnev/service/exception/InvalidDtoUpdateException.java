package agrechnev.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Oleksiy Grechnyev on 12/18/2016.
 * Bad Dto on update
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDtoUpdateException extends RuntimeException {
    public InvalidDtoUpdateException(String message) {
        super(message);
    }

    public InvalidDtoUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
