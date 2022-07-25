package ua.foxminded.university.dao.exception;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public class DAOException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}