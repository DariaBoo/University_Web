package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.TransactionSystemException;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({"/groups.sql"})
class GroupServiceImplTest {
    
    @Autowired
    private GroupService groupService;
    @Autowired
    private LessonService lessonService;

    private Group group;
    private Group updatedGroup;
    private Group theSameGroupName;
    private Lesson lesson;
    
    @BeforeEach
    void setup() {
        group = Group.builder().name("AA-00").departmentId(1).build();
        theSameGroupName = Group.builder().name("AA-00").departmentId(2).build();   
        updatedGroup = Group.builder().name("ZZ-99").departmentId(4).build();
    }
    
    @Test
    void addGroup_shouldReturnSavedGroup_whenInputNewGroup() {
        assertEquals(group, groupService.addGroup(group));
    }
    
    @Test
    void addGroup_shouldThrowDataIntegrityViolationException_whenInputNotUniqueName() {  
        groupService.addGroup(group);
        assertThrows(DataIntegrityViolationException.class, () -> groupService.addGroup(theSameGroupName));
    }
    
    //ConstraintViolationException is a root cause of TransactionSystemException 
    @Test
    void addGroup_shouldThrowConstraintViolationException_whenInputBlankName() { 
        group = Group.builder().name("LongName").departmentId(3).build();
        assertThrows(TransactionSystemException.class, () -> groupService.addGroup(group));
    }
    
    @Test
    void updateGroup_shouldThrowDataIntegrityViolationException_whenInputNotUniqueName() {  
        groupService.addGroup(group);
        groupService.addGroup(updatedGroup);
        updatedGroup = Group.builder().name("AA-00").departmentId(3).build();
        assertThrows(DataIntegrityViolationException.class, () -> groupService.updateGroup(updatedGroup));
    }
    
    @Test
    void assignLessonToGroup_shouldReturnTrue_whenInputExistedGroupIdAndLessonId() {
        lesson = Lesson.builder().name("lesson").description("description").build();
        groupService.addGroup(group);
        lessonService.addLesson(lesson);
        assertTrue(groupService.assignLessonToGroup(group.getId(), lesson.getId()));
    }
    
    @Test
    void deleteLessonFromGroup_shouldReturnTrue() {
        lesson = Lesson.builder().name("lesson").description("description").build();
        lessonService.addLesson(lesson);
        group.setLessons(Collections.singletonList(lesson));
        groupService.addGroup(group);
        assertTrue(groupService.deleteLessonFromGroup(group.getId(), lesson.getId()));
    }
}
