package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.LessonDAOImpl;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Lesson;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Service
public class LessonServiceImpl implements LessonService{
    private final LessonDAOImpl lessonDaoImpl;
    private static final Logger log = LoggerFactory.getLogger(LessonServiceImpl.class.getName());

    /**
     * Returns instance of the class
     * @param lessonDaoImpl
     */
    @Autowired
    public LessonServiceImpl(LessonDAOImpl lessonDaoImpl) {
        this.lessonDaoImpl = lessonDaoImpl;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int addLesson(Lesson lesson) throws ServiceException {
        log.trace("Add new lesson to the database");
        log.trace("Check is lesson name - {} is not out of bound", lesson.getName());
        if(lesson.getName().length() > lessonDaoImpl.getLessonNameMaxSize()) {
            log.error("Lesson name - {} is out of bound", lesson.getName());
            throw new ServiceException("Lesson name is out of bound.");
        }
        if(lesson.getDescription().length() > lessonDaoImpl.getDescriptionMaxSize()) {
            log.error("Lesson description - {} is out of bound", lesson.getDescription());
            throw new ServiceException("Lesson description is out of bound.");
        }       
        return lessonDaoImpl.addLesson(lesson);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLesson(int lessonID) {
        log.trace("Delete lesson with id {}", lessonID);
        return lessonDaoImpl.deleteLesson(lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Lesson> findByID(int lessonID) {
        log.trace("Find lesson by id {}", lessonID);
        return lessonDaoImpl.findByID(lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Lesson>> findAllLessons() {
        log.trace("Find all lessons");
        return lessonDaoImpl.findAllLessons();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateLesson(Lesson lesson) throws ServiceException {
        log.trace("Update lesson name and description");
        log.trace("Check is lesson name - {} is not out of bound.", lesson.getName());
        if(lesson.getName().length() > lessonDaoImpl.getLessonNameMaxSize()) {
            log.error("Lesson name is out of bound");
            throw new ServiceException("Lesson name is out of bound.");
        }
        log.trace("Check is description - {} is not out of bound", lesson.getDescription());
        if(lesson.getDescription().length() > lessonDaoImpl.getDescriptionMaxSize()) {
            log.error("Description is out of bound");
            throw new ServiceException("Lesson description is out of bound.");
        }        
        return lessonDaoImpl.updateLesson(lesson);
    }
}
