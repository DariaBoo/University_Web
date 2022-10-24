package ua.foxminded.university.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.security.model.AuthenticatedUser;
import ua.foxminded.university.service.entities.Role;
import ua.foxminded.university.service.entities.User;

@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplUnitTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private AuthenticatedUser userDetails;
    private User user;

    @BeforeEach
    void setup() {
        Role role = new Role("role");
        role.setId(1);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = User.builder().id(1).firstName("name").lastName("surname").username("username")
                .password("$2a$12$lfZAPSeyNE8pVpmtqZGoAehgRKpegxGU99YougHbUqxRx/so4REWO").roles(roles).build();
        userDetails = new AuthenticatedUser(user);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenInputExistedUsername() {
        given(userDAO.findByUsername(user.getUsername())).willReturn(user);
        assertEquals(userDetails, userDetailsService.loadUserByUsername("username"));
        verify(userDAO, times(1)).findByUsername(any(String.class));
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenInputNotExistedUsername() {
        given(userDAO.findByUsername(any(String.class))).willReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("username"));
        verify(userDAO, times(1)).findByUsername(any(String.class));
    }
}
