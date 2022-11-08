package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.GroupService;
import ua.foxminded.university.service.LessonService;
import ua.foxminded.university.service.TeacherService;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;
import ua.foxminded.university.service.exception.EntityConstraintViolationException;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/groups.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class GroupServiceImplTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RoleDAO roleDao;

    private Group group;
    private Group groupWithLessons;
    private User user;
    private Teacher teacher;
    private Lesson lesson;
    private Role role;

    @BeforeEach
    void setup() {
        group = Group.builder().name("AA-00").departmentId(1).build();
        role = new Role("TEACHER");
        user = User.builder().username("name").firstName("name").lastName("name")
                .roles(Stream.of(role).collect(Collectors.toList())).password("12234").build();
        lesson = Lesson.builder().id(1).name("lesson").description("description")
                .teachers(Stream.of(teacher).collect(Collectors.toList())).build();
        teacher = Teacher.builder().id(1).user(user).position("position")
                .lessons(Stream.of(lesson).collect(Collectors.toList())).build();
        groupWithLessons = Group.builder().name("AA-00").departmentId(1)
                .lessons(Stream.of(lesson).collect(Collectors.toList())).build();

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
    void updateGroup_shouldReturnUpdatedGroup_whenInputCorrectData() {
        groupService.addGroup(group);
        Group updatedGroup = Group.builder().id(1).name("AA-11").departmentId(1).build();
        assertEquals(updatedGroup.getName(), groupService.updateGroup(updatedGroup).getName());
    }

    @Test
    void updateGroup_shouldThrowUniqueConstraintViolationException_whenInputCorrectData() {
        String notUniqueName = "AA-00";
        groupService.addGroup(group);
        Group updatedGroup = Group.builder().id(1).name(notUniqueName).departmentId(1).build();
        assertThrows(EntityConstraintViolationException.class, () -> groupService.updateGroup(updatedGroup));
    }

    @Test
    void deleteGroup_shouldReturnTrue_whenInputExistedId() {
        groupService.addGroup(group);
        groupService.deleteGroup(group.getId());
        assertEquals(0, groupService.findAllGroups().size());
    }

    @Test
    void deleteGroup_shouldThrowEntityNotFoundException_whenInputNotExistedId() {
        int notExistedId = 5;
        assertThrows(EntityNotFoundException.class, () -> groupService.deleteGroup(notExistedId));
    }

    @Test
    void assignLessonToGroup_shouldReturnTrue_whenInputExistedGroupIdAndLessonId() {
        lesson = Lesson.builder().name("lesson").description("description").build();
        groupService.addGroup(group);
        lessonService.addLesson(lesson);
        assertTrue(groupService.assignLessonToGroup(group.getId(), lesson.getId()));
    }

    @Test
    void assignLessonToGroup_shouldReturnFalse_whenInputExistedGroupIdAndLessonId() {
        groupService.addGroup(group);
        assertFalse(groupService.assignLessonToGroup(group.getId(), 1));
    }

    @Test
    void deleteLessonFromGroup_shouldReturnTrue() {
        lesson = Lesson.builder().name("lesson").description("description").build();
        lessonService.addLesson(lesson);
        group.setLessons(Collections.singletonList(lesson));
        groupService.addGroup(group);
        assertTrue(groupService.deleteLessonFromGroup(group.getId(), lesson.getId()));
    }

    @Test
    void deleteLessonFromGroup_shouldReturnFalse() {
        groupService.addGroup(group);
        assertFalse(groupService.deleteLessonFromGroup(group.getId(), 1));
    }

    @Test
    void findAllGroups_shouldReturnList() {
        groupService.addGroup(group);
        Group group2 = group = Group.builder().name("AA-01").departmentId(1).build();
        groupService.addGroup(group2);
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        groups.add(group2);
        assertEquals(groups.size(), groupService.findAllGroups().size());
    }

    @Test
    void findById_shouldReturnGroup_whenInputExistedId() {
        groupService.addGroup(group);
        assertEquals(group, groupService.findById(group.getId()));
    }

    @Test
    void findById_shouldThrowIllegalArgumentException_whenInputNotExistedId() {
        int notExistedId = 5;
        assertThrows(IllegalArgumentException.class, () -> groupService.findById(notExistedId));
    }

    @Test
    void findGroupsByTeacherId_shouldReturnListGroups_whenInputCorrectData() {
        roleDao.save(role);
        userDao.save(user);
        lessonService.addLesson(lesson);
        teacherService.addTeacher(teacher);
        groupService.addGroup(groupWithLessons);
        List<Group> groups = new ArrayList<>();
        groups.add(groupWithLessons);
        assertEquals(groups, groupService.findGroupsByTeacherId(teacher.getId()));
    }
}
