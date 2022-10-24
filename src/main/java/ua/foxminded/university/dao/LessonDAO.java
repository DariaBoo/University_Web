package ua.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.university.service.entities.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 */
public interface LessonDAO extends JpaRepository<Lesson, Integer> {  
    
    /**
     * The method returns lesson by lesson name
     * @param name
     * @return lesson
     */
    Lesson findByName(String name);
}
