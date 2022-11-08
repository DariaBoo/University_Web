package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.StaffDAO;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.entities.Staff;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/staff.sql" })
@Transactional
class StaffDAOTest {

    @Autowired
    private StaffDAO staffDao;
    @Autowired
    private UserDAO userDao;
    private Staff staff;
    private User user;

    @BeforeEach
    void setup() {
        user = User.builder().username("username").firstName("name").lastName("name").password("password").build();
        userDao.saveAndFlush(user);
        staff = new Staff();
        staff.setUser(user);
        staff.setPosition("position");
    }

    @Test
    void findByUserUsername() {
        staffDao.save(staff);
        assertEquals(staff, staffDao.findByUserUsername(staff.getUser().getUsername()));
    }
}
