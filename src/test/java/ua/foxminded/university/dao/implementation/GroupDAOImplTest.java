package ua.foxminded.university.dao.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.entities.Group;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
class GroupDAOImplTest {
        
    @Autowired
    private GroupDAO groupDAO;    

    private List<Group> groups = new ArrayList<Group>();
    private Group group;
    
    @BeforeEach
    void init() throws SQLException {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:tablesTest.sql").build();
    }

    @Test
    void addGroup_shouldReturnGroupID_whenInputCorrectGroup() {
        int countOfGroups = groupDAO.findAllGroups().get().size();
        assertEquals(countOfGroups + 1, groupDAO.addGroup(Group.builder().name("AA-00").departmentID(8).build()));
    }

    @Test
    void addGroup_shouldThrowException_whenInputExistedGroup() {
        group = Group.builder().name("AA-00").departmentID(1).build();
        groupDAO.addGroup(group);
        Group group2 = Group.builder().name("AA-00").departmentID(1).build();  
        assertThrows(DAOException.class, () -> groupDAO.addGroup(group2));
    }

    @Test
    void deleteGroup_shouldReturnCountOfDeletedGroups_whenInputID()  {
        group = Group.builder().name("AA-11").departmentID(0).build();
        int id = groupDAO.addGroup(group);
        int countOfGroups = groupDAO.findAllGroups().get().size();
        groupDAO.deleteGroup(id);
        assertEquals(countOfGroups - 1, groupDAO.findAllGroups().get().size());
    }

    @ParameterizedTest(name = "{index}. When input not existed group id or negative number {0}.")
    @CsvSource({ "100", "-100" })
    void deleteGroup_shouldThrowIllegalArgumentException_whenInputNotExistedID(int groupID) {
         assertFalse(groupDAO.deleteGroup(groupID));
    }

    @Test
    void assignLessonToGroup_shouldReturnCountOfAddedLessons_whenAssignLessonToGroup() {
        int countOfGroups = groupDAO.findGroupsByLessonId(1).get().size();
        groupDAO.assignLessonToGroup(3, 1);
        assertEquals(countOfGroups + 1, groupDAO.findGroupsByLessonId(1).get().size());
    }

//    @Test
//    void assignLessonToGroup_shouldReturnCountOfAddedRows_whenInputAlreadyAssingedGroupAndLesson() {
//        boolean isAssigned = groupDAO.assignLessonToGroup(1, 2);
//        System.out.println("isAssigned " + isAssigned);
//        boolean isAssignedAgain = groupDAO.assignLessonToGroup(1, 2);
//        System.out.println("isAssignedAgain " + isAssignedAgain);
//        assertFalse(isAssignedAgain);
//    }

    @ParameterizedTest(name = "{index}. When input already existed lesson and group or incorect lesson id or group id.")
    @CsvSource({ "1, 8", "1, 100", "100, 8", "100, 100" })
    void assignLessonToGroup_shouldReturnNegativeNumber_whenInputIncorrectData(int lessonID, int groupID) {
        assertFalse(groupDAO.assignLessonToGroup(groupID, lessonID));
    }

    @Test
    void deleteLessonFromGroup_shouldReturnCountOfDelededRows_whenInputExistedLessonIDAndGroupID() {
        groupDAO.assignLessonToGroup(1, 2);
        assertTrue(groupDAO.deleteLessonFromGroup(1, 2));
    }
    
    @Test
    void findAllGroups_shouldReturnAllGroups_whenCallTheMethod() {
        assertEquals(6, groupDAO.findAllGroups().get().size());        
    }
    
    @Test
    void findAllGroups_shouldReturnFirstThreeGroups_whenCallTheMethod() {
        groups.clear();
        groups.add(Group.builder().id(1).name("CO-68").departmentID(1).build());
        groups.add(Group.builder().id(2).name("FW-72").departmentID(1).build());
        groups.add(Group.builder().id(3).name("OQ-07").departmentID(1).build());
        assertEquals(groups, groupDAO.findAllGroups().get().stream().limit(3).collect(Collectors.toList()));        
    }

    @Test
    void updateGroup_shouldReturnOne_whenInputExistedGroupIDAndNewName() {     
        group = Group.builder().id(1).name("00-AA").departmentID(1).build();
        int groupID = groupDAO.addGroup(group);
        group.setName("AA-00");
        groupDAO.updateGroup(group);        
        assertEquals("AA-00", groupDAO.findById(groupID).get().getName());
    }
    @Test
    void updateGroup_whenInputNotExistedGroupID() {   
        group = Group.builder().id(100).name("AA-00").build();
        groupDAO.updateGroup(group);        
        assertEquals("AA-00", groupDAO.findById(100).get().getName());
    }

    @Test
    void findGroupsByTeacherId_shouldReturnCountOfGroups_whenInputTeacherId() {
        assertEquals(4, groupDAO.findGroupsByTeacherId(1).get().size());
    }
}
