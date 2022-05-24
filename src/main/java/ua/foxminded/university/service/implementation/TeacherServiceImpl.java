package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.TeacherDAOImpl;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.exception.ServiceException;
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
     * @throws ServiceException 
     */
    @Override
    public int addTeacher(Teacher teacher) throws ServiceException {
        log.trace("Add new teacher {}", teacher);
        int result = 0;
        log.trace("Check if teacher's first name is not out of bound");
        if (teacher.getFirstName().length() > teacherDAOImpl.getFirstNameMaxSize()) {
            log.error("Teacher's first name is out of bound.");
            throw new ServiceException("Teacher's first name is out of bound.");
        }
        log.trace("Check if teacher's last name is not out of bound");
        if (teacher.getLastName().length() > teacherDAOImpl.getLastNameMaxSize()) {
            log.error("Teacher's last name is out of bound.");
            throw new ServiceException("Teacher's last name is out of bound.");
        }
        log.trace("Check if teacher's position is not out of bound");
        if (teacher.getPosition().length() > teacherDAOImpl.getPositionMaxSize()) {
            log.error("Teacher's position is out of bound.");
            throw new ServiceException("Teacher's position is out of bound.");
        }
        log.trace("Check if teacher's password is not out of bound");
        if (teacher.getPassword().length() > teacherDAOImpl.getPasswordMaxSize()) {
            log.error("Teacher's password is out of bound.");
            throw new ServiceException("Teacher's password is out of bound.");
        }
        result = teacherDAOImpl.addTeacher(teacher);
        log.debug("Took a result {} of adding a new teacher", result);
        return result; // TODO ui if result == 1 a teacher was added correctly, if == 0 - error occurred
                       // while added a teacher or the teacher already exist
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTeacher(int teacherID) {
        log.trace("Delete existed teacher by id {}", teacherID);
        int result = teacherDAOImpl.deleteTeacher(teacherID);
        log.debug("Took the result {} of deleting teacher from the database", result);
        return result;// TODO ui if result == 1 a teacher was deleted correctly, if == 0 - error occurred
                      // while trying to delete a teacher
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
    public int changePosition(int teacherID, String position) throws ServiceException {
        log.trace("Change teacher's position to {}, teacher id {}", position, teacherID);
        if(position.length() > teacherDAOImpl.getPositionMaxSize()) {
            log.error("Position - {} is out of bound", position);
            throw new ServiceException("Position is out of bound");
        }
        return teacherDAOImpl.changePosition(teacherID, position);
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
    public Optional<Teacher> findByID(int teacherID) {
        log.trace("Find teacher by id {}", teacherID);
        return teacherDAOImpl.findByID(teacherID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findAllTeachers() {
        log.trace("Find all teachers");
        return teacherDAOImpl.findAllTeachers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Teacher>> findTeachersByDepartment(int departmentID) {
        log.trace("Find teachers by department with id {}", departmentID);
        return teacherDAOImpl.findTeachersByDepartment(departmentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changePassword(int teacherID, String newPassword) throws ServiceException {
        log.trace("Change teacher's password");
        log.info("Check is new password is not bigger then 10 symbols");
        if(newPassword.length() > teacherDAOImpl.getPasswordMaxSize()) {
            log.error("Password can't be more than 10 symbols. Password length {}", newPassword.length());
            throw new ServiceException("Password can't be more than 10 symbols");
        }
        return teacherDAOImpl.changePassword(teacherID, newPassword);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateTeacher(Teacher teacher) throws ServiceException {
        log.trace("Update existed teacher {}", teacher);
        int result = 0;
        log.trace("Check if teacher's first name is not out of bound");
        if (teacher.getFirstName().length() > teacherDAOImpl.getFirstNameMaxSize()) {
            log.error("Teacher first name is out of bound.");
            throw new ServiceException("Teacher first name is out of bound.");
        }
        log.trace("Check if teacher's last name is not out of bound");
        if (teacher.getLastName().length() > teacherDAOImpl.getLastNameMaxSize()) {
            log.error("Teachert last name is out of bound.");
            throw new ServiceException("Teacher last name is out of bound.");
        }
        result = teacherDAOImpl.updateTeacher(teacher);
        log.debug("Took a result {} of updating a teacher", result);
        return result;
    }   
}
