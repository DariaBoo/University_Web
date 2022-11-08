package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.StudentDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.entities.Group;
import ua.foxminded.university.service.entities.Student;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/students2.sql" })
@Transactional
class StudentDAOTest {

    @Autowired
    private StudentDAO studentDao;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private GroupDAO groupDao;
    private Student student;
    private User user;

    @BeforeEach
    void setup() {
        user = User.builder().username("username").firstName("name").lastName("name").password("password").build();
        userDao.saveAndFlush(user);
        Group group = Group.builder().name("AA-00").departmentId(1).build();
        groupDao.saveAndFlush(group);
        student = Student.builder().user(user).group(group).idCard("abc").build();
    }

    @Test
    void save() {
        assertNotNull(studentDao.save(student));
        assertEquals(student, studentDao.save(student));
    }

    @Test
    void findById() {
        studentDao.save(student);
        Student result = studentDao.findById(student.getId()).get();
        assertEquals(student, result);
    }

    @Test
    void findAll() {
        studentDao.save(student);
        List<Student> students = new ArrayList<>();
        students.add(student);
        assertEquals(students, studentDao.findAll());
    }

    @Test
    void deleteById() {
        studentDao.save(student);
        studentDao.deleteById(student.getId());
        assertEquals(0, studentDao.findAll().size());
    }

    @Test
    void existById() {
        studentDao.save(student);
        assertTrue(studentDao.existsById(student.getId()));
    }

    @Test
    void findByUser() {
        studentDao.save(student);
        assertEquals(student, studentDao.findByUser(student.getUser()).get());
    }

    @Test
    void findByUserUsername() {
        studentDao.save(student);
        assertEquals(student, studentDao.findByUserUsername(student.getUser().getUsername()));
    }
}
