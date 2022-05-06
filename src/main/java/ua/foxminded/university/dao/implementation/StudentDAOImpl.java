package ua.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.service.pojo.Student;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 *
 */
@Component
public class StudentDAOImpl implements StudentDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String ADD_STUDENT = "INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) SELECT ?, ?, ?, 1234, ?, true "
            + "WHERE NOT EXISTS (SELECT first_name, last_name FROM timetable.students WHERE first_name = ? AND last_name = ?) "
            + "AND EXISTS (SELECT group_id FROM timetable.groups WHERE group_id = ?)";
    private final String DELETE_STUDENT = "UPDATE timetable.students SET isActive = false WHERE student_id = ?;";
    private final String CHANGE_GROUP = "UPDATE timetable.students SET group_id = ? WHERE student_id = ? "
            + "AND EXISTS (SELECT group_id FROM timetable.groups WHERE group_id = ?) "
            + "AND EXISTS (SELECT student_id FROM timetable.students WHERE student_id = ?)";
    private static final Logger log = LoggerFactory.getLogger(StudentDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     * @param groupDAOImpl
     */
    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbcTemplate, GroupDAOImpl groupDAOImpl) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addStudent(Student student) {
        log.info("Add new student to timetable.students and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_STUDENT, student.getFirstName(), student.getLastName(), student.getGroupID(),
                student.getIdCard(), student.getFirstName(), student.getLastName(), student.getGroupID());// rowmapper
                                                                                                          // ???
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteStudent(int studentID) {
        log.info("Delete student from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_STUDENT, studentID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changeGroup(int studentID, int groupID) {
        log.info("Execute sql to change group id at the timetable.students and return count of updated rows otherwise returns zero");
        return jdbcTemplate.update(CHANGE_GROUP, groupID, studentID, groupID, studentID);
    }
}
