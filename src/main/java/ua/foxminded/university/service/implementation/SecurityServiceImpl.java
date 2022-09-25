package ua.foxminded.university.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.foxminded.university.security.UserDetailsServiceImpl;
import ua.foxminded.university.service.SecurityService;
import ua.foxminded.university.service.exception.ServiceException;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public boolean login(String username, String password) {
        try {
            UserDetails authorisedUser = userDetailsService.loadUserByUsername(username);
            System.out.println("SERVICE------------------------" + authorisedUser.getUsername()
                    + authorisedUser.getPassword() + authorisedUser.getAuthorities());
            if (authorisedUser.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        } catch (UsernameNotFoundException e) {
            throw new ServiceException(e.getMessage());
        }
        return false;
    }
}
