package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.config.HibernateConfigTest;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.exception.ServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
class GroupServiceImplTest {
    
    @Autowired
    private GroupService groupService;

    private Group group = new Group();
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;
    
    @BeforeEach
    void init() {
        new EmbeddedDatabaseBuilder().setName("test").setType(EmbeddedDatabaseType.H2)
        .addScript("classpath:tablesTest.sql").build();
    }

    @Test
    void addGroup_shouldReturnResult_whenInputCorrectGroupName() throws ServiceException {
        group = Group.builder().name("AA-00").departmentID(1).build();
        assertEquals(7, groupService.addGroup(group));
    }
    @Test
    void addGroup_shouldThrowServiceExceptionMessage_whenInputNotUniqueName() {
        group = Group.builder().name("AA-00").departmentID(1).build();
        groupService.addGroup(group);
        Group group2 = Group.builder().name("AA-00").departmentID(1).build();
        exception = assertThrows(ServiceException.class, () -> groupService.addGroup(group2));
        expectedMessage = "Group with name AA-00 already exists!";
        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
