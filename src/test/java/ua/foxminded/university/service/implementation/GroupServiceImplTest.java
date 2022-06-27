package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.foxminded.university.config.SpringConfigTest;
import ua.foxminded.university.service.exception.ServiceException;
import ua.foxminded.university.service.pojo.Group;

@TestInstance(Lifecycle.PER_CLASS)
class GroupServiceImplTest {
    private GroupServiceImpl groupServiceImpl;
    private AnnotationConfigApplicationContext context;
    private Group group;
    private Exception exception;
    private String expectedMessage;
    private String actualMessage;
    private int maxGroupNameSize = 5;
    
    @BeforeAll
    void init() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        groupServiceImpl = context.getBean("groupServiceImpl", GroupServiceImpl.class);
    }

    @Test
    void addGroup_shouldThrowServiceException_whenInputIncorrectGroupName() {
        group = new Group.GroupBuilder().setName(createCountOfSymbols(maxGroupNameSize + 1)).setDepartmentID(1).build();
        assertThrows(StringIndexOutOfBoundsException.class, () -> groupServiceImpl.addGroup(group));
    }
    
    @Test
    void addGroup_shouldReturnResult_whenInputCorrectGroupName() throws ServiceException {
        group = new Group.GroupBuilder().setName("AA-00").setDepartmentID(1).build();
        assertEquals(1, groupServiceImpl.addGroup(group));
    }
    @Test
    void addGroup_shouldThrowServiceExceptionMessage_whenInputIncorrectGroupName() {
        group = new Group.GroupBuilder().setName(createCountOfSymbols(maxGroupNameSize + 1)).setDepartmentID(1).build();
        exception = assertThrows(StringIndexOutOfBoundsException.class, () -> groupServiceImpl.addGroup(group));
        expectedMessage = "Group name is out of bound";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void addGroup_shouldThrowServiceExceptionMessage_whenInputIncorrectGroupName2() {
        group = new Group.GroupBuilder().setName("88-uu").setDepartmentID(1).build();
        exception = assertThrows(ServiceException.class, () -> groupServiceImpl.addGroup(group));
        expectedMessage = "Group name should contain two letters, dash and two digits";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void addGroup_shouldThrowServiceExceptionMessage_whenInputIncorrectGroupName3() {
        group = new Group.GroupBuilder().setName("uu44").setDepartmentID(1).build();
        exception = assertThrows(ServiceException.class, () -> groupServiceImpl.addGroup(group));
        expectedMessage = "Group name should contain two letters, dash and two digits";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void updateGroup_shouldReturnResult_whenInputCorrectGroupName() throws ServiceException {
        group = new Group.GroupBuilder().setID(1).setName("ZZ-00").setDepartmentID(1).build();
        assertEquals(1, groupServiceImpl.updateGroup(group));
    }

    private String createCountOfSymbols(int count) {
        return Stream.generate(() -> "a")
                .limit(count)
                .collect(Collectors.joining());
    }
}
