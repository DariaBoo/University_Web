package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Repository
public interface TeacherDAO  extends JpaRepository<Teacher, Integer> {

}
