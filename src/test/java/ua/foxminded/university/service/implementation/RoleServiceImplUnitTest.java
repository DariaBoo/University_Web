package ua.foxminded.university.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.service.entities.Role;

@ExtendWith(SpringExtension.class)
class RoleServiceImplUnitTest {

    @Mock
    private RoleDAO roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(1);
        role.setName("Role");
    }

    @Test
    void addRole_shouldReturnAddedRole_whenAddNewRole() {
        given(roleDao.save(role)).willReturn(role);
        assertEquals(role, roleService.addRole(role));
        verify(roleDao, times(1)).save(any(Role.class));
    }

    @Test
    void deleteRole() {
        int roleId = 1;
        willDoNothing().given(roleDao).deleteById(roleId);
        roleService.deleteRole(roleId);
        verify(roleDao, times(1)).deleteById(roleId);
    }

    @Test
    void findByName_shoulReturnRole_whenInputExistedRolesName() {
        given(roleDao.findByName(any(String.class))).willReturn(Optional.of(role));
        assertEquals(role, roleService.findByName("Role"));
        verify(roleDao, times(1)).findByName(any(String.class));
    }

    @Test
    void findByName_shouldThrowIllegalArgumentException_whenInputNotExistedRolesName() {
        given(roleDao.findByName(any(String.class))).willReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> roleService.findByName("none"));
    }
}
