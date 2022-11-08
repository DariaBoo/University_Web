package ua.foxminded.university.dao.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.service.entities.Role;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/roles.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class RoleDAOTest {

    @Autowired
    private RoleDAO roleDao;
    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(1);
        role.setName("role");
    }

    @Test
    void save() {
        assertNotNull(roleDao.save(role));
        assertEquals(role, roleDao.save(role));
        assertNotNull(roleDao.findByName("role"));
    }

    @Test
    void deleteById() {
        roleDao.save(role);
        roleDao.deleteById(role.getId());
        assertEquals(0, roleDao.findAll().size());
    }

    @Test
    void findByName() {
        roleDao.save(role);
        assertEquals(role, roleDao.findByName(role.getName()).get());
    }
}
