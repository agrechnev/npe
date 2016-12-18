package agrechnev.service;

/**
 * Created by Oleksiy Grechnyev on 12/18/2016.
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
