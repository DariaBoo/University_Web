package ua.foxminded.university.service.exception;

public class ServiceException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
