package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.implementation.mapper.GroupMapper;
import ua.foxminded.university.service.pojo.Group;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Repository
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
    private final String FIND_ALL_GROUPS = "SELECT * FROM timetable.groups WHERE isActive = true ORDER BY group_id;";
    private final String FIND_GROUPS_BY_DEPARTMENT = "SELECT * FROM timetable.groups WHERE department_id = ? AND isActive = true ORDER BY group_id;";
    private final String UPDATE_GROUP = "UPDATE timetable.groups SET group_name = ? WHERE group_id = ? AND NOT EXISTS (SELECT group_name FROM timetable.groups WHERE group_name = ?) AND EXISTS (SELECT group_id FROM timetable.groups);";
    private final String GROUP_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('groups') AND UPPER (column_name) = UPPER ('group_name');";
    private static final Logger log = LoggerFactory.getLogger(GroupDAOImpl.class.getName());

    /**
     * Returns instance of the class
     * 
     * @param jdbcTemplate
     */
    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addGroup(Group group) {
        log.trace("Add new group to timetable.groups and returns count of added rows otherwise returns zero");
        return jdbcTemplate.update(ADD_GROUP, group.getName(), group.getDepartmentID(), group.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteGroup(int groupID) {
        log.trace("Delete group from the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_GROUP, groupID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToGroup(int groupID, int lessonID) {
        log.trace("Assign lesson to group to the database and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(ASSIGN_LESSON_TO_GROUP, groupID, lessonID, groupID, lessonID, groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromGroup(int groupID, int lessonID) {
        log.trace(
                "Delete lesson from group from the timetable.groups_lessons and returns count of deleted rows otherwise returns zero");
        return jdbcTemplate.update(DELETE_LESSON_FROM_GROUP, groupID, lessonID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findAllGroups() {
        log.debug("Find all groups from timetable.groups");
        return Optional.of(jdbcTemplate.query(FIND_ALL_GROUPS, new GroupMapper()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findGroupsByDepartment(int departmentID) {
        log.debug("Find all groups by department from timetable.groups");
        return Optional.of(jdbcTemplate.query(FIND_GROUPS_BY_DEPARTMENT, new Object[] { departmentID }, new GroupMapper()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateGroup(Group group) {
        log.trace("Update group's name to {}", group.getName());
        return jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getID(), group.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGroupNameMaxSize() {
        log.trace("Get the column 'group_name' size");
        return jdbcTemplate.queryForObject(GROUP_NAME_MAX_SIZE, Integer.class);
    }
}
