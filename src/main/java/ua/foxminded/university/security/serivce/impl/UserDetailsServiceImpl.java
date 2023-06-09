package ua.foxminded.university.security.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.UserDAO;
import ua.foxminded.university.security.model.AuthenticatedUser;
import ua.foxminded.university.service.entities.User;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            log.warn("[ON loadUserByUsername]:: user with username::[ {} ] not found", username);
            throw new UsernameNotFoundException("User not found for username - " + username);
        }
        log.info("[ON loadUserByUsername]:: loaded user with username [ {} ] and roles [ {} ]", user.getUsername(),
                user.getRoles());
        return new AuthenticatedUser(user);
    }
}
