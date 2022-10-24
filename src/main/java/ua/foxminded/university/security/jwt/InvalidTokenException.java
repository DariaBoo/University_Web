package ua.foxminded.university.security.jwt;

import io.jsonwebtoken.JwtException;

public class InvalidTokenException extends JwtException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public InvalidTokenException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public InvalidTokenException(String message, Throwable cause) {
        super(message);
    }
}
