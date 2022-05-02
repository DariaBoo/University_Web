package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Group;

@Component
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class GroupDAOImplTest {
    private GroupDAOImpl groupDAOImpl;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        groupDAOImpl = context.getBean("groupDAOImpl", GroupDAOImpl.class);
    }

    @Test
    void addGroup_shouldReturnNegativeNumber_whenInputExistedGroup() throws SQLException {
        assertEquals(-1, groupDAOImpl.addGroup(new Group.GroupBuilder().setName("FW-72").setDepartmentID(8).build()));
    }

    @Test
    void addGroup_shouldReturnAddedGroupID_whenInputNewGroup() throws SQLException {
        assertEquals(7, groupDAOImpl.addGroup(new Group.GroupBuilder().setName("AA-00").setDepartmentID(0).build()));
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
        assertEquals(-1, groupDAOImpl.assignLessonToGroup(1, 2));
    }

    @ParameterizedTest(name = "{index}. When input already existed lesson and group or incorect lesson id or group id will return negative number {2}.")
    @CsvSource({ "1, 8, -1", "1, 100, -1", "100, 8, -1", "100, 100, -1" })
    void assignLessonToGroup_shouldReturnNegativeNumber_whenInputIncorrectData(int lessonID, int groupID, int result) {
        assertEquals(result, groupDAOImpl.assignLessonToGroup(lessonID, groupID));
    }

    @Test
    void deleteLessonFromGroup_shouldReturnCountOfDelededRows_whenInputExistedLessonIDAndGroupID() {
        groupDAOImpl.assignLessonToGroup(1, 2);
        assertEquals(1, groupDAOImpl.deleteLessonFromGroup(1, 2));
    }

    @ParameterizedTest(name = "{index}. When input not existed or incorrect lessonID {0} and not existed or incorrect groupID {1} will return negative number {2}.")
    @CsvSource({ "1, 3, -1", "1, 100, -1", "100, 8, -1", "100, 100, -1" })
    void deleteLessonFromGroup_shouldReturnNegativeNumber_whenInputNotExistedLessonIDInTheGroup(int lessonID,
            int groupID, int result) {
        assertEquals(result, groupDAOImpl.deleteLessonFromGroup(lessonID, groupID));
    }

    @Test
    void isExists_shourtReturnTrue_whenInputExistedGroupID() {
        assertTrue(groupDAOImpl.isGroupExists(1));
    }

    @ParameterizedTest(name = "{index}. When input not existed group id or negative number or zero will return false.")
    @ValueSource(ints = { 100, -1, 0 })
    void isExists_shourtReturnFalse_whenInputNotExistedGroupID(int groupID) {
        assertFalse(groupDAOImpl.isGroupExists(groupID));
    }

    @Test
    void isGroupHasLesson_shouldReturnTrue_whenInputExistedLessonIDAndGroupID() {
        groupDAOImpl.assignLessonToGroup(1, 3);
        assertTrue(groupDAOImpl.isGroupHasLesson(1, 3));
    }

    @Test
    void isGroupHasLesson_shouldReturnFalse_whenInputNotExistedLessonIDAndGroupID() {
        groupDAOImpl.assignLessonToGroup(1, 3);
        groupDAOImpl.deleteLessonFromGroup(1, 3);
        assertFalse(groupDAOImpl.isGroupHasLesson(1, 3));
    }
}
