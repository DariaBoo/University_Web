package ua.foxminded.university.service.exception;

import javax.naming.AuthenticationException;

public class InvalidUserException extends AuthenticationException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public InvalidUserException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public InvalidUserException(String message, Throwable cause) {
        super(message);
    }
}
