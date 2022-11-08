package ua.foxminded.university.service.implementation.integration_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import ua.foxminded.university.AppSpringBoot;
import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.entities.Role;

@SpringBootTest(classes = AppSpringBoot.class)
@Sql({ "/groups.sql" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleDAO roleDAO;
    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(1);
        role.setName("role");
    }

    @Test
    void findByName_shouldReturnRole_whenInputCorrectName() {
        roleService.addRole(role);
        assertEquals(role, roleService.findByName(role.getName()));
    }

    @Test
    void findByName_shouldThrowIllegalArgumentException_whenInputInCorrectName() {
        String notExistedName = "someName";
        assertThrows(IllegalArgumentException.class, () -> roleService.findByName(notExistedName));
    }

    @Test
    void addRole_shouldReturnRole_whenInputCorrectData() {
        assertEquals(role, roleService.addRole(role));
    }

    @Test
    void deleteRole_shouldReturnEntity_whenInputExistedId() {
        roleService.addRole(role);
        int size = roleDAO.findAll().size();
        roleService.deleteRole(role.getId());
        assertEquals(size - 1, roleDAO.findAll().size());
    }

    @Test
    void deleteRole_shouldThrowEntityNotFoundException_whenInputNotExistedId() {
        int notExistedId = roleDAO.findAll().size() + 1;
        assertThrows(EntityNotFoundException.class, () -> roleService.deleteRole(notExistedId));
    }
}
