package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.pojo.Group;

class GroupDAOImplTest {
    private GroupDAOImpl groupDAOImpl;
    private AnnotationConfigApplicationContext context;
    private List<Group> groups = new ArrayList<Group>();
    private int maxGroupNameSize = 5;

    @BeforeEach
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        groupDAOImpl = context.getBean("groupDAOImpl", GroupDAOImpl.class);
    }

    @Test
    void addGroup_shouldReturnZero_whenInputExistedGroup() {
       assertEquals(0, groupDAOImpl.addGroup(new Group.GroupBuilder().setName("FW-72").setDepartmentID(8).build()));
    }

    @Test
    void addGroup_shouldReturnCountOfAddedRows_whenInputNewGroup() {
        assertEquals(1, groupDAOImpl.addGroup(new Group.GroupBuilder().setName("AA-00").setDepartmentID(0).build()));//TODO add department entity????
    }

    @Test
    void deleteGroup_shouldReturnCountOfDeletedGroups_whenInputID() {
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
    
    @Test
    void findAllGroups_shouldReturnAllGroups_whenCallTheMethod() {
        assertEquals(6, groupDAOImpl.findAllGroups().get().stream().count());        
    }
    
    @Test
    void findAllGroups_shouldReturnFirstThreeGroups_whenCallTheMethod() {
        groups.clear();
        groups.add(new Group.GroupBuilder().setID(1).setName("CO-68").setDepartmentID(1).build());
        groups.add(new Group.GroupBuilder().setID(2).setName("FW-72").setDepartmentID(1).build());
        groups.add(new Group.GroupBuilder().setID(3).setName("OQ-07").setDepartmentID(1).build());
        assertEquals(groups, groupDAOImpl.findAllGroups().get().stream().limit(3).collect(Collectors.toList()));        
    }
    
    @Test
    void findGroupsByDepartment_shouldReturnCountOFGroups_whenCallTheMethod() {
        assertEquals(5, groupDAOImpl.findGroupsByDepartment(1).get().stream().count());
    }
    
    @Test
    void findGroupsByDepartment_shouldReturnDepartmentsGroups_whenInputExistedDepartmentID() {
        groups.clear();
        groups.add(new Group.GroupBuilder().setID(6).setName("OB-14").setDepartmentID(2).build());
        assertEquals(groups, groupDAOImpl.findGroupsByDepartment(2).get());
    }
    @Test
    void findGroupsByDepartment_shouldReturnEmptyOptional_whenInputNotExistedDepartmentID() {        
        assertEquals(Optional.of(new ArrayList<Group>()), groupDAOImpl.findGroupsByDepartment(20));
    }
    @Test
    void updateGroup_shouldReturnOne_whenInputExistedGroupIDAndNewName() {       
        assertEquals(1, groupDAOImpl.updateGroup(new Group.GroupBuilder().setID(5).setName("AA-00").build()));
    }
    @Test
    void updateGroup_shouldReturnOne_whenInputNotExistedGroupID() {       
        assertEquals(0, groupDAOImpl.updateGroup(new Group.GroupBuilder().setID(100).setName("AA-00").build()));
    }
    @Test
    void getGroupNameMaxSize_shouldReturnColumnSize_whenCallTheMethod() {
        assertEquals(maxGroupNameSize, groupDAOImpl.getGroupNameMaxSize());
    }
    @Test
    void findGroupsByTeacherId_shouldReturnCountOfGroups_whenInputTeacherId() {
        assertEquals(4, groupDAOImpl.findGroupsByTeacherId(1).get().size());
    }
}
