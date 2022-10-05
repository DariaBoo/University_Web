package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.exception.ServiceException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppSpringBoot.class)
@Sql({"/groups.sql"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class GroupServiceImplTest {
    
    @Autowired
    private GroupService groupService;

    private Group group = Group.builder().name("AA-00").departmentId(1).build();
    private Group group2 = Group.builder().name("AA-00").departmentId(1).build();
    private String expectedMessage = "Group with name - [AA-00] already exists";
    
    @Test
    void addGroup_shouldReturnResult_whenInputCorrectGroupName() throws ServiceException {
        assertTrue(groupService.addGroup(group));
    }
    
    @Test
    void addGroup_shouldThrowServiceExceptionMessage_whenInputNotUniqueName() {
        groupService.addGroup(group);        
        Exception exception = assertThrows(UniqueConstraintViolationException.class, () -> groupService.addGroup(group2));
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
