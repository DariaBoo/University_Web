package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.User;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface UserDAO extends JpaRepository<User, Integer>{
    
    User findByUsername(String username);
}
