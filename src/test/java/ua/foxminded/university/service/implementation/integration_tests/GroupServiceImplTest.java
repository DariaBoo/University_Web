package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/groups.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class GroupServiceImplTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private LessonService lessonService;

    private Group group;

    private Lesson lesson;

    @BeforeEach
    void setup() {
        group = Group.builder().name("AA-00").departmentId(1).build();
    }

    @Test
    void addGroup_shouldReturnSavedGroup_whenInputNewGroup() {
        assertEquals(group, groupService.addGroup(group));
    }

    @Test
    void addGroup_shouldThrowConstraintViolationException_whenInputLongName() {
        group = Group.builder().name("LongName").departmentId(3).build();
        assertThrows(ConstraintViolationException.class, () -> groupService.addGroup(group));
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
