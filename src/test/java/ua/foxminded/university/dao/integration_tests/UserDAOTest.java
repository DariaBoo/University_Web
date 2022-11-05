package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.User;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/users.sql" })
@Transactional
class UserDAOTest {

    @Autowired
    private UserDAO userDao;
    private User user;

    @BeforeEach
    void setup() {
        user = User.builder().username("username").firstName("name").lastName("name").password("password")
                .roles(new ArrayList<Role>()).build();
    }

    @Test
    void findByUsername() {
        userDao.save(user);
        assertEquals(user, userDao.findByUsername(user.getUsername()));
    }
}
