package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.User;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface UserDAO extends JpaRepository<User, Integer> {

    /**
     * Finds user by username
     * 
     * @param username
     * @return user
     */
    User findByUsername(String username);
}
