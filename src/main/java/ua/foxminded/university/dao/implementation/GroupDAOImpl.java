package ua.foxminded.university.dao.implementation;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    private final LessonDAOImpl lessonDAOImpl;
    private final String DATABASE_NAME = "university";
    private final String ADD_GROUP = "INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES (?, ?, true) RETURNING group_id";
    private final String ADD_GROUP_TEST = "INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES (?, ?, true)";
    private final String DELETE_GROUP = "UPDATE timetable.groups SET isActive = false WHERE group_id = ?;";
    private final String IS_NAME_UNIQUE = "SELECT COUNT(*) FROM timetable.groups WHERE group_name = ?";
    private final String IS_GROUP_EXISTS = "SELECT COUNT(*) FROM timetable.groups WHERE group_id = ? AND isActive = true";
    private final String ADD_LESSON = "INSERT INTO timetable.groups_lessons (group_id, lesson_id) VALUES (?, ?)";
    private final String DELETE_LESSON = "DELETE FROM timetable.groups_lessons WHERE group_id = ? AND lesson_id = ?";
    private final String IS_GROUP_HAS_LESSON = "SELECT COUNT(*) FROM timetable.groups_lessons WHERE group_id = ? AND lesson_id = ?";
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
        this.lessonDAOImpl = lessonDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addGroup(Group group) throws SQLException {
        log.trace("Start group adding method");
        KeyHolder key = new GeneratedKeyHolder();
        int result = -1;
        log.debug(
                "Choose correct sql query depends on the database: if work database = ADD_GROUP, if test database = ADD_GROUP_TEST");
        String sql = jdbcTemplate.getDataSource().getConnection().getCatalog().equals(DATABASE_NAME) ? ADD_GROUP
                : ADD_GROUP_TEST;
        log.info("Check if group name is unique in the timetable.groups table");
        if (isNameUnique(group.getName())) {
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, group.getName());
                statement.setInt(2, group.getDepartmentID());
                return statement;
            }, key);
            log.info("Executed sql query to add group to the database");
            result = key.getKey().intValue();
            log.debug("Took added group id {}", result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteGroup(int groupID) { 
        log.debug(
                "Execute sql to delete group from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_GROUP, groupID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToGroup(int groupID, int lessonID) {
        int result = -1;
        log.info(
                "Check if group and lesson are exist in the timetable.groups and timetable.lessons and check if the group has the lesson");
        if (isGroupExists(groupID) && lessonDAOImpl.isLessonExists(lessonID) && !isGroupHasLesson(groupID, lessonID)) {
            result = jdbcTemplate.update(ADD_LESSON, groupID, lessonID);
            log.debug("Assign lesson to group to the timetable.groups_lessons and took the result {}", result);
        } else {
            log.info(
                    "Group or lesson is not exists in the tables timetable.groups and timetable.lessons or this group doesn't have this lesson");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromGroup(int groupID, int lessonID) {
        int result = -1;
        log.info(
                "Check if group and lesson are exist in the timetable.groups and timetable.lessons and check if the group has the lesson");
        if (isGroupExists(groupID) && lessonDAOImpl.isLessonExists(lessonID) && isGroupHasLesson(groupID, lessonID)) {
            result = jdbcTemplate.update(DELETE_LESSON, groupID, lessonID);
            log.debug("Delete lesson from group from the timetable.groups_lessons and took the result {}", result);
        } else {
            log.info(
                    "Group or lesson is not exists in the tables timetable.groups and timetable.lessons or this group doesn't have this lesson");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGroupHasLesson(int groupID, int lessonID) {
        log.trace("Check if group with id {} has lesson with id {} in the timetable.groups_lessons", groupID, lessonID);
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_GROUP_HAS_LESSON, new Object[] { groupID, lessonID },
                Integer.class);
        log.debug("Executed sql to take count of existed rows {} in the timetable.groups_lessons", countOfRows);
        if (countOfRows != 0) {
            result = true;
            log.info("Group with id {} has lesson with id {} in the timetable.groups_lessons", groupID, lessonID);
        } else {
            log.info("This group {} doesn't have this lesson {}", groupID, lessonID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGroupExists(int groupID) {
        log.trace("Check if group id is exists in the timetable.groups");
        boolean result = false;
        int countOfRows = jdbcTemplate.queryForObject(IS_GROUP_EXISTS, new Object[] { groupID }, Integer.class);
        log.debug("Executed sql to take count of rows {} in the timetable.groups with group id {}", countOfRows,
                groupID);
        if (countOfRows != 0) {
            result = true;
            log.info("Group with id {} exists in the timetable.groups", groupID);
        } else {
            log.info("Group with id {} doesn't exist in the timetable.groups", groupID);
        }
        return result;
    }

    private boolean isNameUnique(String groupName) {
        log.trace("Check if group name is unique in the timetable.groups");
        boolean result = true;
        int countOfRows = jdbcTemplate.queryForObject(IS_NAME_UNIQUE, new Object[] { groupName }, Integer.class);
        log.debug("Executed sql to take count of rows {} in the timetable.groups with group name {}", countOfRows,
                groupName);
        if (countOfRows != 0) {
            result = false;
            log.info("Group with name {} is not unique and already exists in the timetable.groups", groupName);
        } else {
            log.info("The group with name {} is unique and not exists in the database", groupName);
        }
        return result;
    }
}
