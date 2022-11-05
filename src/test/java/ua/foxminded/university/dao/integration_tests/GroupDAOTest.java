package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.LessonDAO;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Lesson;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/groups.sql" })
@Transactional
class GroupDAOTest {

    @Autowired
    private GroupDAO groupDao;
    @Autowired
    private LessonDAO lessonDao;
    @Autowired
    private TeacherDAO teacherDao;
    @Autowired
    private UserDAO userDao;
    private Group group;
    private Teacher teacher;

    @BeforeEach
    void setup() {
        group = Group.builder().name("AA-00").departmentId(1).build();
    }

    @Test
    void save() {
        assertNotNull(groupDao.save(group));
        assertEquals(group, groupDao.save(group));
        assertNotNull(groupDao.findByName("AA-00"));
    }

    @Test
    void findById() {
        groupDao.save(group);
        Group result = groupDao.findById(group.getId()).get();
        assertEquals(group, result);
    }

    @Test
    void findAll() {
        groupDao.save(group);
        List<Group> groups = new ArrayList<Group>();
        groups.add(group);
        assertEquals(groups, groupDao.findAll());
    }

    @Test
    void deleteById() {
        groupDao.save(group);
        groupDao.deleteById(group.getId());
        assertEquals(0, groupDao.findAll().size());
    }

    @Test
    void deleteById_throwsException() {
        int notExistedId = 5;
        assertThrows(EmptyResultDataAccessException.class, () -> groupDao.deleteById(notExistedId));
    }

    @Test
    void existById() {
        groupDao.save(group);
        assertTrue(groupDao.existsById(group.getId()));
    }

    private void prepareData() {
        User user = User.builder().username("name").firstName("name").lastName("name").password("12234").build();
        userDao.saveAndFlush(user);
        Lesson lesson = Lesson.builder().id(1).name("lesson").description("description")
                .teachers(Stream.of(teacher).collect(Collectors.toList())).build();
        lessonDao.saveAndFlush(lesson);
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        teacher = Teacher.builder().id(1).user(user).position("position").lessons(lessons).build();
        teacherDao.saveAndFlush(teacher);
        group = Group.builder().name("AA-00").departmentId(1).lessons(lessons).build();
        groupDao.saveAndFlush(group);
    }

    @Test
    @Transactional
    void findByLessons_Teachers_Id() {
        prepareData();
        List<Group> groups = groupDao.findAll();
        assertEquals(groups, groupDao.findByLessons_Teachers_Id(teacher.getId()).get());
    }

    @Test
    void findByName() {
        groupDao.save(group);
        assertEquals(group, groupDao.findByName(group.getName()));
    }
}
