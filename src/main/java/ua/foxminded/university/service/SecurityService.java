package ua.foxminded.university.service;

import ua.foxminded.university.service.exception.InvalidUserException;

public interface SecurityService {

    boolean isAuthenticated(String username, String password)  throws InvalidUserException;
}
