package ua.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface StudentDAO extends JpaRepository<Student, Integer> {

    /**
     * Finds by username
     * @param username
     * @return student
     */
    Student findByUserUsername(String username);
    
    /**
     * Finds by user
     * @param user
     * @return optional student
     */
    Optional<Student> findByUser(User user);
}
