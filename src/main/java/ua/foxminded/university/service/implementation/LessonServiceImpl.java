package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class LessonServiceImpl implements LessonService {
     
    
    @Autowired
    private LessonDAO lessonDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int addLesson(Lesson lesson) {  
        int result = 0;
        try {     
            result = lessonDAO.addLesson(lesson);
        } catch (DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteLesson(int lessonID) {
        return lessonDAO.deleteLesson(lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateLesson(Lesson lesson) {
        try {
        log.debug("Update lesson - {}", lesson);
        lessonDAO.updateLesson(lesson);
        } catch (DAOException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Lesson findByID(int lessonID) {
        Lesson resultLesson = lessonDAO.findByID(lessonID).orElseThrow(() -> new IllegalArgumentException("Error occured while searching lesson by id"));
        log.debug("Find lesson by id - {} and return lesson - {}", lessonID, resultLesson);
        return resultLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Lesson> findAllLessons() {
        List<Lesson> resultList = lessonDAO.findAllLessons().orElseThrow(() -> new IllegalArgumentException("Error occured while searching all lessons"));
        log.debug("Take a list of lessons - {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Lesson> findLessonsByTeacherId(int teacherID) {
        Set<Lesson> resultSet = lessonDAO.findLessonsByTeacherId(teacherID).orElseThrow(() -> new IllegalArgumentException("Error occured while searching lessons by teacher id"));
        log.debug("Find all lessons by teacher id - {} and return list of lessons - {}", teacherID, resultSet);
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Lesson> findLessonsByGroupId(int groupID) {
        Set<Lesson> resultSet = lessonDAO.findLessonsByGroupId(groupID).orElseThrow(() -> new IllegalArgumentException("Error occured while searching lessons by group id"));
        log.debug("Find all lessons by group id - {} and return list of lessons - {}", groupID, resultSet);
        return resultSet;
    }
}
