package ua.foxminded.university.dao.implementation;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.service.pojo.Group;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Component
public class GroupDAOImpl implements GroupDAO {
    private final JdbcTemplate jdbcTemplate;
    private final String ADD_GROUP = "INSERT INTO timetable.groups (group_name, department_id, isActive) SELECT ?, ?, 'true' "
            + "WHERE NOT EXISTS (SELECT group_name FROM timetable.groups WHERE group_name = ?)";
    private final String DELETE_GROUP = "UPDATE timetable.groups SET isActive = false WHERE group_id = ?;";
    private final String ASSIGN_LESSON_TO_GROUP = "INSERT INTO timetable.groups_lessons (group_id, lesson_id) SELECT ?, ?"
            + " WHERE NOT EXISTS (SELECT group_id, lesson_id FROM timetable.groups_lessons WHERE group_id = ? AND lesson_id = ?) "
            + "AND EXISTS (SELECT group_id FROM timetable.groups WHERE group_id = ?)\n"
            + "            AND EXISTS (SELECT lesson_id FROM timetable.lessons WHERE lesson_id = ?)";
    private final String DELETE_LESSON_FROM_GROUP = "DELETE FROM timetable.groups_lessons WHERE group_id = ? AND lesson_id = ?";
    private static final Logger log = LoggerFactory.getLogger(GroupDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     * @param lessonDAOImpl
     */
    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbcTemplate, LessonDAOImpl lessonDAOImpl) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addGroup(Group group) throws SQLException {
        log.info("Add new group to timetable.groups and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_GROUP, group.getName(), group.getDepartmentID(), group.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteGroup(int groupID) {
        log.info("Delete group from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_GROUP, groupID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToGroup(int groupID, int lessonID) {
        log.info("Assign lesson to group to the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(ASSIGN_LESSON_TO_GROUP, groupID, lessonID, groupID, lessonID, groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromGroup(int groupID, int lessonID) {
        log.info(
                "Delete lesson from group from the timetable.groups_lessons and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON_FROM_GROUP, groupID, lessonID);
    }
}
