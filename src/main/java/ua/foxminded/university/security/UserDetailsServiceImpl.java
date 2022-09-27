package ua.foxminded.university.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.security.model.AuthorisedUser;
import ua.foxminded.university.service.entities.User;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.findByUsername(username);
        log.info("LOGIN AS ADMIN/USER-------------------------------------------");
        if (user == null) {
            throw new UsernameNotFoundException("User not found for username" + username);
        }
        log.info("ADMIN/USER ROLE------------------------------" + user.getRoles().get(0).getName());
        return new AuthorisedUser(user);
    }
}
