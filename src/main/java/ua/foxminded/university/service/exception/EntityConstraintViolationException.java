package ua.foxminded.university.service.exception;

public class EntityConstraintViolationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6777545321714572308L;

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public EntityConstraintViolationException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public EntityConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
