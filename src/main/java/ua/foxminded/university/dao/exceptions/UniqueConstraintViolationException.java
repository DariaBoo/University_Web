package ua.foxminded.university.dao.exceptions;

public class UniqueConstraintViolationException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public UniqueConstraintViolationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public UniqueConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
