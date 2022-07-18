package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Day;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.exception.ServiceException;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class.getName());
    int result = 0;

    @Autowired
    private TeacherDAO teacherDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int addTeacher(Teacher teacher) {
        try {
            result = teacherDAO.addTeacher(teacher);
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
    public void updateTeacher(Teacher teacher) {
        try {
            teacherDAO.updateTeacher(teacher);
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
    public boolean deleteTeacher(int teacherID) {
        return teacherDAO.deleteTeacher(teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean assignLessonToTeacher(int lessonID, int teacherID) {
        return teacherDAO.assignLessonToTeacher(lessonID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteLessonFromTeacher(int lessonID, int teacherID) {
        return teacherDAO.deleteLessonFromTeacher(lessonID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int setTeacherAbsent(int teacherID, Day day) {
        return teacherDAO.setTeahcerAbsent(teacherID, day);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteTeacherAbsent(int teacherID, Day day) {
        teacherDAO.deleteTeahcerAbsent(teacherID, day);
        log.debug("Delete teacher with id {} absent in a day - {} and return a result - {}", teacherID, day);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Teacher findByID(int teacherID) {
        Teacher resultTeacher = teacherDAO.findByID(teacherID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured while searching by id"));
        log.debug("Find teacher by id {}  and return a result - {}", teacherID, resultTeacher);
        return resultTeacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Teacher> findAllTeachers() {
        List<Teacher> resultList = teacherDAO.findAllTeachers()
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Return list of all teachers - {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Teacher> findTeachersByLessonId(int lessonID) {
        Set<Teacher> resultSet = teacherDAO.findTeachersByLessonId(lessonID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find teachers by lesson id {} and return a result - {}", lessonID, resultSet);
        return resultSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int changePassword(int teacherID, String newPassword) {
        result = teacherDAO.changePassword(teacherID, newPassword);
        log.debug("Return a result - {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Day> showTeacherAbsent(int teacherID) {
        List<Day> resultList = teacherDAO.showTeacherAbsent(teacherID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured while displaying teacher absent"));
        log.debug("Return a list with teachers absent days - {}", resultList);
        return resultList;
    }
}
