package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface StudentDAO extends JpaRepository<Student, Integer> {

    Student findByUsername(String username);
}
