package ua.foxminded.university.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Slf4j
@Service
public class TeacherServiceImpl implements TeacherService {

    int result = 0;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private LessonDAO lessonDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean addTeacher(Teacher teacher) {
        try {
            teacherDAO.save(teacher);
            log.info("Save teacher with id :: {}", teacher.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Teacher with first name [" + teacher.getFirstName() + "] and last name ["
                    + teacher.getLastName() + "] already exists!");
        }
        return teacherDAO.existsById(teacher.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateTeacher(Teacher teacher) {
        try {
            teacherDAO.save(teacher);
            log.info("Update teacher with id :: {}", teacher.getId());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Teacher with first name [" + teacher.getFirstName() + "] and last name ["
                    + teacher.getLastName() + "] already exists!");
        }
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    @Transactional
    public boolean deleteTeacher(int teacherId) {
        teacherDAO.deleteById(teacherId);
        log.info("Delete teacher by id :: {}", teacherId);
        return !teacherDAO.existsById(teacherId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean assignLessonToTeacher(int lessonId, int teacherId) {
        Optional<Teacher> teacher = teacherDAO.findById(teacherId);
        Optional<Lesson> lesson = lessonDAO.findById(lessonId);
        if (teacher.isPresent() && lesson.isPresent()) {
            teacher.get().getLessons().add(lesson.get());
            teacherDAO.save(teacher.get());
            log.info("Assign lesson [id::{}] to teacher [id::{}]", lessonId, teacherId);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteLessonFromTeacher(int lessonId, int teacherId) {
        Optional<Teacher> teacher = teacherDAO.findById(teacherId);
        Optional<Lesson> lesson = lessonDAO.findById(lessonId);
        if (teacher.isPresent() && lesson.isPresent()) {
            teacher.get().getLessons().remove(lesson.get());
            teacherDAO.save(teacher.get());
            log.info("Delete lesson [id::{}] from teacher [id::{}]", lessonId, teacherId);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean setTeacherAbsent(int teacherId, Day day) {
        Optional<Teacher> teacher = teacherDAO.findById(teacherId);
        if (teacher.isPresent()) {
            teacher.get().getAbsentPeriod().add(day);
            teacherDAO.save(teacher.get());
            log.info("Set absent period to teacher [id::{}]", teacherId);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteTeacherAbsent(int teacherId, Day day) {
        Optional<Teacher> teacher = teacherDAO.findById(teacherId);
        if (teacher.isPresent()) {
            teacher.get().getAbsentPeriod().remove(day);
            teacherDAO.save(teacher.get());
            log.info("Delete absent period from teacher [id::{}]", teacherId);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Teacher findById(int teacherId) {
        Teacher resultTeacher = teacherDAO.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Error occured while searching by id"));
        resultTeacher.getAbsentPeriod();
        log.debug("Found teacher by id :: {}", teacherId);
        return resultTeacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable("teachers")
    public List<Teacher> findAllTeachers() {
        List<Teacher> resultList = teacherDAO.findAll();
        log.debug("Found all teachers, list size - {}", resultList.size());
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void changePassword(int teacherId, String newPassword) {
        Teacher teacher = findById(teacherId);        
        teacher.setPassword(newPassword);
        teacherDAO.save(teacher);
        log.debug("Password changed. Teacher id :: {}", teacherId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional()    
    public boolean checkIsAbsent(LocalDate date, Teacher teacher) {
        Predicate<Day> isBefore = day ->  day.getDateOne().minusDays(1).isBefore(date);
        Predicate<Day> isAfter = day ->  day.getDateTwo().plusDays(1).isAfter(date);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        boolean isAbsent = teacher.getAbsentPeriod().stream().anyMatch(isBefore.and(isAfter));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.info("Check is teacher [id::{}] absent [date::{}]", teacher.getId(), date);
        return isAbsent;
    }
}
