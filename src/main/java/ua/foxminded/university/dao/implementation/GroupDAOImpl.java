package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.implementation.mappers.GroupMapper;
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
    private final String FIND_BY_ID = "SELECT * FROM timetable.groups WHERE group_id = ?;";
    private final String FIND_GROUPS_BY_LESSON_ID = "SELECT * FROM timetable.groups AS tg WHERE group_id IN (SELECT group_id FROM timetable.groups_lessons WHERE lesson_id = ?);";
    private final String FIND_GROUPS_BY_DEPARTMENT = "SELECT * FROM timetable.groups WHERE department_id = ? AND isActive = true ORDER BY group_id;";
    private final String UPDATE_GROUP = "UPDATE timetable.groups SET group_name = ? WHERE group_id = ? AND NOT EXISTS (SELECT group_name FROM timetable.groups WHERE group_name = ?) AND EXISTS (SELECT group_id FROM timetable.groups);";
    private final String FIND_GROUPS_BY_TEACHER_ID = "SELECT DISTINCT tg.* FROM timetable.groups AS tg\n"
            + "LEFT JOIN timetable.groups_lessons AS tgl ON tgl.group_id = tg.group_id \n"
            + "WHERE tgl.lesson_id IN (SELECT lesson_id FROM timetable.lessons_teachers WHERE teacher_id = ?) ORDER BY group_id;";
    private final String GROUP_NAME_MAX_SIZE = "SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.columns WHERE UPPER (table_schema) = UPPER ('timetable') AND UPPER (table_name) = UPPER ('groups') AND UPPER (column_name) = UPPER ('group_name');";
    private static final Logger log = LoggerFactory.getLogger(GroupDAOImpl.class.getName());
    private final String debugMessage = "Return count of rows otherwise returns zero. The result is {}";
    private int result;

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
        log.trace("Add new group to timetable.groups");
        result = jdbcTemplate.update(ADD_GROUP, group.getName(), group.getDepartmentID(), group.getName());
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteGroup(int groupID) {
        log.trace("Delete group from the database");
        result = jdbcTemplate.update(DELETE_GROUP, groupID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int assignLessonToGroup(int groupID, int lessonID) {
        log.trace("Assign lesson to group to the database");
        result = jdbcTemplate.update(ASSIGN_LESSON_TO_GROUP, groupID, lessonID, groupID, lessonID, groupID, lessonID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLessonFromGroup(int groupID, int lessonID) {
        log.trace("Delete lesson from group from the timetable.groups_lessons");
        result = jdbcTemplate.update(DELETE_LESSON_FROM_GROUP, groupID, lessonID);
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findAllGroups() {
        log.trace("Find all groups from timetable.groups");
        Optional<List<Group>> result = Optional.of(jdbcTemplate.query(FIND_ALL_GROUPS, new GroupMapper()));
        log.debug("Return optional list of groups {}", result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findGroupsByLessonId(int lessonID) {
        log.trace("Find all groups by lesson id {}", lessonID);
        Optional<List<Group>> result = Optional.of(jdbcTemplate.query(FIND_GROUPS_BY_LESSON_ID, new Object[] { lessonID }, new GroupMapper()));
        log.debug("Return optional list of groups {}", result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findGroupsByDepartment(int departmentID) {
        log.trace("Find all groups by department from timetable.groups");
        Optional<List<Group>> result = Optional.of(jdbcTemplate.query(FIND_GROUPS_BY_DEPARTMENT, new Object[] { departmentID }, new GroupMapper()));
        log.debug("Return optional list of groups {}", result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Group>> findGroupsByTeacherId(int teacherID) {
        log.trace("Find all groups by teacher id");
        Optional<List<Group>> result = Optional.of(jdbcTemplate.query(FIND_GROUPS_BY_TEACHER_ID, new Object[] { teacherID }, new GroupMapper()));
        log.debug("Return optional list of groups {}", result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Group> findById(int groupID) {
        log.trace("Find group by id {}", groupID);
        Optional<Group> resultList = jdbcTemplate
                .query(FIND_BY_ID, new Object[] { groupID }, new GroupMapper()).stream().findFirst();
        log.debug("Return optional group {}", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateGroup(Group group) {
        log.trace("Update group's name to {}", group.getName());
        result = jdbcTemplate.update(UPDATE_GROUP, group.getName(), group.getId(), group.getName());
        log.debug(debugMessage, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGroupNameMaxSize() {
        return jdbcTemplate.queryForObject(GROUP_NAME_MAX_SIZE, Integer.class);
    }

}
