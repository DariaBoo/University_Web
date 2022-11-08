package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.TeacherDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.entities.Teacher;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/teachers.sql" })
@Transactional
class TeacherDAOTest {

    @Autowired
    private TeacherDAO teacherDao;
    @Autowired
    private UserDAO userDao;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setup() {
        user = User.builder().username("username").firstName("name").lastName("name").password("password").build();
        userDao.saveAndFlush(user);
        teacher = Teacher.builder().user(user).departmentId(1).position("position").build();
    }

    @Test
    void save() {
        assertNotNull(teacherDao.save(teacher));
        assertEquals(teacher, teacherDao.save(teacher));
    }

    @Test
    void findById() {
        teacherDao.save(teacher);
        Teacher result = teacherDao.findById(teacher.getId()).get();
        assertEquals(teacher, result);
    }

    @Test
    void findAll() {
        teacherDao.save(teacher);
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        assertEquals(teachers, teacherDao.findAll());
    }

    @Test
    void deleteById() {
        teacherDao.save(teacher);
        teacherDao.deleteById(teacher.getId());
        assertEquals(0, teacherDao.findAll().size());
    }

    @Test
    void existById() {
        teacherDao.save(teacher);
        assertTrue(teacherDao.existsById(teacher.getId()));
    }

    @Test
    void findByUserUsername() {
        teacherDao.save(teacher);
        assertEquals(teacher, teacherDao.findByUserUsername(teacher.getUser().getUsername()));
    }
}
