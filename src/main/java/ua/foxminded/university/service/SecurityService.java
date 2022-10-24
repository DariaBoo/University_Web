package ua.foxminded.university.service;

import ua.foxminded.university.service.exception.InvalidUserException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface SecurityService {

    /**
     * The method checks if user authenticated by username and password
     * @param username
     * @param password
     * @return true or false
     * @throws InvalidUserException
     */
    boolean isAuthenticated(String username, String password)  throws InvalidUserException;
}
