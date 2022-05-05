package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Group;
import ua.foxminded.university.dao.implementation.GroupDAOImpl;


@Component
@TestInstance(Lifecycle.PER_CLASS)
class GroupDAOImplTest {
    private GroupDAOImpl groupDAOImpl;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        groupDAOImpl = context.getBean("groupDAOImpl", GroupDAOImpl.class);
    }

    @Test
    void addGroup_shouldReturnZero_whenInputExistedGroup() throws SQLException {
       assertEquals(0, groupDAOImpl.addGroup(new Group.GroupBuilder().setName("FW-72").setDepartmentID(8).build()));
    }

    @Test
    void addGroup_shouldReturnCountOfAddedRows_whenInputNewGroup() throws SQLException {
        assertEquals(1, groupDAOImpl.addGroup(new Group.GroupBuilder().setName("AA-00").setDepartmentID(0).build()));
    }

    @Test
    void deleteGroup_shouldReturnCountOfDeletedGroups_whenInputID() throws SQLException {
        int id = groupDAOImpl.addGroup(new Group.GroupBuilder().setName("AA-11").setDepartmentID(0).build());
        assertEquals(1, groupDAOImpl.deleteGroup(id));
    }

    @ParameterizedTest(name = "{index}. When input not existed group id or negative number {0} will return zero {1}.")
    @CsvSource({ "100, 0", "-100, 0" })
    void deleteGroup_shouldReturnZero_whenInputNotExistedID(int groupID, int result) {
        assertEquals(result, groupDAOImpl.deleteGroup(groupID));
    }

    @Test
    void assignLessonToGroup_shouldReturnCountOfAddedRows_whenInputExistedLessonIDAndGroupIDAndRowNotExists() {
        assertEquals(1, groupDAOImpl.assignLessonToGroup(1, 2));
    }

    @Test
    void assignLessonToGroup_shouldReturnCountOfAddedRows_whenInputExistedLessonIDAndGroupIDAndRowExists() {
        groupDAOImpl.assignLessonToGroup(1, 2);
        assertEquals(0, groupDAOImpl.assignLessonToGroup(1, 2));
    }

    @ParameterizedTest(name = "{index}. When input already existed lesson and group or incorect lesson id or group id will return negative number {2}.")
    @CsvSource({ "1, 8, 0", "1, 100, 0", "100, 8, 0", "100, 100, 0" })
    void assignLessonToGroup_shouldReturnNegativeNumber_whenInputIncorrectData(int lessonID, int groupID, int result) {
        assertEquals(result, groupDAOImpl.assignLessonToGroup(lessonID, groupID));
    }

    @Test
    void deleteLessonFromGroup_shouldReturnCountOfDelededRows_whenInputExistedLessonIDAndGroupID() {
        groupDAOImpl.assignLessonToGroup(1, 2);
        assertEquals(1, groupDAOImpl.deleteLessonFromGroup(1, 2));
    }

    @ParameterizedTest(name = "{index}. When input not existed or incorrect lessonID {0} and not existed or incorrect groupID {1} will return negative number {2}.")
    @CsvSource({ "1, 3, 0", "1, 100, 0", "100, 8, 0", "100, 100, 0" })
    void deleteLessonFromGroup_shouldReturnNegativeNumber_whenInputNotExistedLessonIDInTheGroup(int lessonID,
            int groupID, int result) {
        assertEquals(result, groupDAOImpl.deleteLessonFromGroup(lessonID, groupID));
    }
}
