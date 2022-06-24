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
public class TeacherServiceImpl implements TeacherService{
    private final TeacherDAOImpl teacherDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class.getName());

    /**
     * Returns instance of the class
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
        log.trace("Update existed teacher {}", teacher);
        int result = 0;
        result = teacherDAOImpl.updateTeacher(teacher);
        log.debug("Took a result {} of updating a teacher", result);
        return result;
    }  

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        log.trace("Delete existed teacher by id {}", teacherID);
        return teacherDAOImpl.deleteTeacher(teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToTeacher(int lessonID, int teacherID) {
        log.trace("Assign lesson with id {} to teacher with id {}", lessonID, teacherID);        
        return teacherDAOImpl.assignLessonToTeacher(lessonID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromTeacher(int lessonID, int teacherID) {
        log.trace("Delete lesson with id {} from teacher with id {}", lessonID, teacherID);
        return teacherDAOImpl.deleteLessonFromTeacher(lessonID, teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int setTeacherAbsent(int teacherID, Day day) {
        log.trace("Set teacher with id {} absent in a day - {}",teacherID, day);
        return teacherDAOImpl.setTeahcerAbsent(teacherID, day);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacherAbsent(int teacherID, Day day) {
        log.trace("Delete teacher with id {} absent in a day - {}", teacherID, day);
        return teacherDAOImpl.deleteTeahcerAbsent(teacherID, day);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher findByID(int teacherID) {
        log.trace("Find teacher by id {}", teacherID);
        return teacherDAOImpl.findByID(teacherID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> findAllTeachers() {
        log.trace("Find all teachers");
        return teacherDAOImpl.findAllTeachers().orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> findTeachersByDepartment(int departmentID) {
        log.trace("Find teachers by department with id {}", departmentID);
        return teacherDAOImpl.findTeachersByDepartment(departmentID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> findTeachersByLessonId(int lessonID) {
        return teacherDAOImpl.findTeachersByLessonId(lessonID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int teacherID, String newPassword) {
        log.trace("Change teacher's password");
        log.info("Check is new password is not bigger then 10 symbols");
        if(newPassword.length() > teacherDAOImpl.getPasswordMaxSize()) {
            log.error("Password can't be more than 10 symbols. Password length {}", newPassword.length());
            throw new StringIndexOutOfBoundsException("Password can't be more than 10 symbols");
        }
        return teacherDAOImpl.changePassword(teacherID, newPassword);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> showTeacherAbsent(int teacherID) {
        return teacherDAOImpl.showTeacherAbsent(teacherID).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

}
