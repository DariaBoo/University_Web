package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.TeacherDAOImpl;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.pojo.Day;
import ua.foxminded.university.service.pojo.Teacher;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDAOImpl teacherDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param teacherDAOImpl
     */
    @Autowired
    public TeacherServiceImpl(TeacherDAOImpl teacherDAOImpl) {
        this.teacherDAOImpl = teacherDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addTeacher(Teacher teacher) {
        log.trace("Add new teacher {}", teacher);
        int result = 0;
        log.trace("Check if teacher's first name is not out of bound");
        if (teacher.getFirstName().length() > teacherDAOImpl.getFirstNameMaxSize()) {
            log.error("Teacher's first name - {} is out of bound.", teacher.getFirstName());
            throw new StringIndexOutOfBoundsException("Teacher's first name is out of bound.");
        }
        log.trace("Check if teacher's last name is not out of bound");
        if (teacher.getLastName().length() > teacherDAOImpl.getLastNameMaxSize()) {
            log.error("Teacher's last name -{} is out of bound.", teacher.getLastName());
            throw new StringIndexOutOfBoundsException("Teacher's last name is out of bound.");
        }
        log.trace("Check if teacher's position is not out of bound");
        if (teacher.getPosition().length() > teacherDAOImpl.getPositionMaxSize()) {
            log.error("Teacher's position - {} is out of bound.", teacher.getPosition());
            throw new StringIndexOutOfBoundsException("Teacher's position is out of bound.");
        }
        log.trace("Check if teacher's password is not out of bound");
        if (teacher.getPassword().length() > teacherDAOImpl.getPasswordMaxSize()) {
            log.error("Teacher's password is out of bound.");
            throw new StringIndexOutOfBoundsException("Teacher's password is out of bound.");
        }
        result = teacherDAOImpl.addTeacher(teacher);
        log.debug("Took a result {} of adding a new teacher", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateTeacher(Teacher teacher) {
        int result = teacherDAOImpl.updateTeacher(teacher);
        log.debug("Took a result {} of updating a teacher", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        int result = teacherDAOImpl.deleteTeacher(teacherID);
        log.debug("Delete existed teacher by id {} and return a result - {}", teacherID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToTeacher(int lessonID, int teacherID) {
        int result = teacherDAOImpl.assignLessonToTeacher(lessonID, teacherID);
        log.debug("Assign lesson with id {} to teacher with id {} and return a result - {}", lessonID, teacherID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromTeacher(int lessonID, int teacherID) {
        int result = teacherDAOImpl.deleteLessonFromTeacher(lessonID, teacherID);
        log.debug("Delete lesson with id {} from teacher with id {} and return a result - {}", lessonID, teacherID, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeacherAbsent(int teacherID, Day day) {
        int result = teacherDAOImpl.setTeahcerAbsent(teacherID, day);
        log.debug("Set teacher with id {} absent in a day - {} and return a result - {}", teacherID, day, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacherAbsent(int teacherID, Day day) {
        int result = teacherDAOImpl.deleteTeahcerAbsent(teacherID, day);
        log.debug("Delete teacher with id {} absent in a day - {} and return a result - {}", teacherID, day, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher findByID(int teacherID) {
        Teacher resultTeacher = teacherDAOImpl.findByID(teacherID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find teacher by id {}  and return a result - {}", teacherID, resultTeacher);
        return resultTeacher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> findAllTeachers() {
        List<Teacher> resultList = teacherDAOImpl.findAllTeachers().orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Return list of all teachers - {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> findTeachersByDepartment(int departmentID) {
        List<Teacher> resultList = teacherDAOImpl.findTeachersByDepartment(departmentID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find teachers by department with id {} and return a result - {}", departmentID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> findTeachersByLessonId(int lessonID) {
        List<Teacher> resultList = teacherDAOImpl.findTeachersByLessonId(lessonID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Find teachers by lesson id {} and return a result - {}", lessonID, resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int teacherID, String newPassword) {
        log.trace("Change teacher's password");
        log.info("Check is new password is not bigger then 10 symbols");
        if (newPassword.length() > teacherDAOImpl.getPasswordMaxSize()) {
            log.error("Password can't be more than 10 symbols. Password length {}", newPassword.length());
            throw new StringIndexOutOfBoundsException("Password can't be more than 10 symbols");
        }
        int result = teacherDAOImpl.changePassword(teacherID, newPassword);
        log.debug("Return a result - {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> showTeacherAbsent(int teacherID) {
        List<Teacher> resultList = teacherDAOImpl.showTeacherAbsent(teacherID)
                .orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Return a list with teachers absent days - {}", resultList);
        return resultList;
    }
}
