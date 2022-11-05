package ua.foxminded.university.service.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;
import ua.foxminded.university.service.exception.UserNotFoundException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Slf4j
@Service
public class TeacherServiceImpl implements TeacherService {

    private final String defaultPassword = "555";
    private final String defaultRole = "TEACHER";
    private final List<Role> roles = new ArrayList<>();
    @Autowired
    private TeacherDAO teacherDAO;
    @Autowired
    private LessonDAO lessonDAO;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Teacher addTeacher(Teacher teacher) {
        Teacher savedTeacher = teacherDAO.findByUserUsername(teacher.getUser().getUsername());
        if (savedTeacher == null) {
            savedTeacher = teacherDAO.save(setDefaultData(teacher));
            log.info("Saved a teacher with id :: {}", teacher.getId());
        } else {
            log.warn("Teacher already exist with id [{}]", savedTeacher.getId());
            throw new EntityConstraintViolationException(
                    "Teacher with username - [" + teacher.getUser().getUsername() + "] already exists!");
        }
        return savedTeacher;
    }

    public Teacher setDefaultData(Teacher teacher) {
        roles.add(roleService.findByName(defaultRole));
        teacher.getUser().setRoles(roles);
        teacher.getUser().setPassword(passwordEncoder.encode(defaultPassword));
        return teacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Teacher updateTeacher(Teacher teacher) {
        Teacher updatedTeacher = new Teacher();
        if (teacherDAO.existsById(teacher.getId())) {
            updatedTeacher = teacherDAO.save(teacher);
            log.info("Updated teacher with id :: {}", teacher.getId());
        }
        return updatedTeacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteTeacher(int teacherId) {
        if (teacherDAO.existsById(teacherId)) {
            teacherDAO.deleteById(teacherId);
            log.info("Delete teacher by id :: {}", teacherId);
        } else {
            log.warn("Teacher with id {} doesn't exist. Nothing to delete.");
            throw new EntityNotFoundException("Teacher with id " + teacherId + " doesn't exist. No teacher to delete.");

        }
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
            log.info("Set absent period {} - {} to teacher [id::{}]", day.getDateOne(), day.getDateTwo(), teacherId);
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
        teacher.getUser().setPassword(passwordEncoder.encode(newPassword));
        teacherDAO.save(teacher);
        log.debug("Password changed. Teacher id :: {}", teacherId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional()
    public boolean checkIsAbsent(LocalDate date, int teacherId) {
        Teacher teacher = findById(teacherId);
        Predicate<Day> isBefore = day -> day.getDateOne().minusDays(1).isBefore(date);
        Predicate<Day> isAfter = day -> day.getDateTwo().plusDays(1).isAfter(date);
        boolean isAbsent = teacher.getAbsentPeriod().stream().anyMatch(isBefore.and(isAfter));
        log.info("Check if teacher [id::{}] is absent [date::{}] - {}", teacher.getId(), date, isAbsent);
        return isAbsent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher findByUsername(String username) {
        Teacher teacher = teacherDAO.findByUserUsername(username);
        if (teacher == null) {
            throw new UserNotFoundException("Teacher with username " + username + " is not exist");
        }
        return teacher;
    }
}
