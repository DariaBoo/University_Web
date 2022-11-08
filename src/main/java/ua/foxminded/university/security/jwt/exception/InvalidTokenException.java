package ua.foxminded.university.security.jwt.exception;

import io.jsonwebtoken.JwtException;

public class InvalidTokenException extends JwtException {

    /**
     * 
     */
    private static final long serialVersionUID = 6225772747415632463L;

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
