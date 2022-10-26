package ua.foxminded.university.service.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2641205415627864621L;

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @author Bogush Daria
     * 
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @author Bogush Daria
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
