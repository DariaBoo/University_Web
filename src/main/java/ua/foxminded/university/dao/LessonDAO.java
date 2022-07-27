package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.service.entities.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 */
@Repository
public interface LessonDAO extends JpaRepository<Lesson, Integer> {  
    
}
