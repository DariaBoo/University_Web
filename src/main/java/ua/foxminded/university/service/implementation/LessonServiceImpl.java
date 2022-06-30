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
public class LessonServiceImpl implements LessonService {

    private final LessonDAOImpl lessonDaoImpl;
    private static final Logger log = LoggerFactory.getLogger(LessonServiceImpl.class.getName());

    /**
     * Returns instance of the class
     * 
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
        if (lesson.getName().length() > lessonDaoImpl.getLessonNameMaxSize()) {
            log.error("Lesson name - {} is out of bound", lesson.getName());
            throw new StringIndexOutOfBoundsException("Lesson name is out of bound.");
        }
        if (lesson.getDescription().length() > lessonDaoImpl.getDescriptionMaxSize()) {
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
        int result = lessonDaoImpl.deleteLesson(lessonID);
        log.debug("Delete lesson with id {} and return a result - {}", lessonID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateLesson(Lesson lesson) {
        int result = lessonDaoImpl.updateLesson(lesson);
        log.debug("Update lesson - {} and return a result - {}", lesson, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lesson findByID(int lessonID) {
        Lesson resultLesson = lessonDaoImpl.findByID(lessonID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find lesson by id - {} and return lesson - {}", lessonID, resultLesson);
        return resultLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findAllLessons() {
        List<Lesson> resultList = lessonDaoImpl.findAllLessons();
        log.debug("Take a list of lessons - {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findLessonsByTeacherId(int teacherID) {
        List<Lesson> resultList = lessonDaoImpl.findLessonsByTeacherId(teacherID);
        log.debug("Find all lessons by teacher id - {} and return list of lessons - {}", teacherID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lesson> findLessonsByGroupId(int groupID) {
        List<Lesson> resultList = lessonDaoImpl.findLessonsByGroupId(groupID);
        log.debug("Find all lessons by group id - {} and return list of lessons - {}", groupID, resultList);
        return resultList;
    }
}
