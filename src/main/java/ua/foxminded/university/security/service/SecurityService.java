package ua.foxminded.university.security.service;

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
     */
    boolean isAuthenticated(String username, String password);
}
