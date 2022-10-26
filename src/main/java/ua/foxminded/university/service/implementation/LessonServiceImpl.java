package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;

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
    public Lesson addLesson(Lesson lesson) {
        Lesson savedLesson = lessonDAO.findByName(lesson.getName());
        if(savedLesson == null) {
            savedLesson = lessonDAO.save(lesson);
            log.info("Added a new lesson with id::{}", lesson.getId());
        } else {
            log.warn("Lesson with name [{}] already exists with id [{}]", savedLesson.getName(), savedLesson.getId());
            throw new UniqueConstraintViolationException("Lesson with name [" + lesson.getName() + "] already exists!");
        }
        return savedLesson;
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
    public Lesson updateLesson(Lesson lesson) {
        Lesson updatedLesson = lessonDAO.findByName(lesson.getName());
        if(updatedLesson == null) {
            updatedLesson = lessonDAO.save(lesson);
            log.info("Update lesson with id::{}", lesson.getId());
        } else {
            log.warn("Lesson with name [{}] already exists with id [{}]", updatedLesson.getName(), updatedLesson.getId());
            throw new UniqueConstraintViolationException("Lesson with name [" + lesson.getName() + "] already exists!");
        }
        return updatedLesson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Lesson findById(int lessonId) {
        Lesson resultLesson = lessonDAO.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Error occured while searching lesson by id"));
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
        log.debug("Found all lessons, list size :: {}", resultList.size());
        return resultList;
    }
}
