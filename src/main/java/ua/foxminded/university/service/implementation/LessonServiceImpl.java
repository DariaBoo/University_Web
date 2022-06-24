package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.LessonDAOImpl;
import ua.foxminded.university.service.LessonService;
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
    public int addLesson(Lesson lesson) {
        log.trace("Add new lesson to the database");
        log.trace("Check is lesson name - {} is not out of bound", lesson.getName());
        if(lesson.getName().length() > lessonDaoImpl.getLessonNameMaxSize()) {
            log.error("Lesson name - {} is out of bound", lesson.getName());
            throw new StringIndexOutOfBoundsException("Lesson name is out of bound.");
        }
        if(lesson.getDescription().length() > lessonDaoImpl.getDescriptionMaxSize()) {
            log.error("Lesson description - {} is out of bound", lesson.getDescription());
            throw new StringIndexOutOfBoundsException("Lesson description is out of bound.");
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
    public int updateLesson(Lesson lesson) {
        log.trace("Update lesson name and description");           
        return lessonDaoImpl.updateLesson(lesson);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lesson findByID(int lessonID) {
        log.trace("Find lesson by id {}", lessonID);
        return lessonDaoImpl.findByID(lessonID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findAllLessons() {
        log.trace("Find all lessons");
        return lessonDaoImpl.findAllLessons();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findLessonsByTeacherId(int teacherID) {
        log.trace("Find all lessons by teacher id - {}", teacherID);
        return lessonDaoImpl.findLessonsByTeacherId(teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findLessonsByGroupId(int groupID) {
        log.trace("Find all lessons by group id - {}", groupID);
        return lessonDaoImpl.findLessonsByGroupId(groupID);
    }
}
