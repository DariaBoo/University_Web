package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
public interface TeacherDAO  extends JpaRepository<Teacher, Integer> {

    Teacher findByUsername(String username);
}
