package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;

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
    public boolean addLesson(Lesson lesson) {  
        try {     
            lessonDAO.save(lesson);
            log.info("Save lesson with id :: {}", lesson.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Lesson with name [" + lesson.getName() + "] already exists!");
        }
        return lessonDAO.existsById(lesson.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteLesson(int lessonId) {
        lessonDAO.deleteById(lessonId);
        log.info("Delete lesson with id :: {}", lessonId);
        return !lessonDAO.existsById(lessonId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateLesson(Lesson lesson) {
        try {
        log.info("Update lesson with id :: {}", lesson.getId());
        lessonDAO.save(lesson);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Lesson with name [" + lesson.getName() + "] already exists!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Lesson findById(int lessonId) {
        Lesson resultLesson = lessonDAO.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("Error occured while searching lesson by id"));
        log.debug("Found lesson by id :: {}", lessonId);
        return resultLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable("lessons")
    public List<Lesson> findAllLessons() {
        List<Lesson> resultList = lessonDAO.findAll();
        log.debug("Found all lessons, list size :: ", resultList.size());
        return resultList;
    }
}
