package ua.foxminded.university.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.service.SecurityService;
import ua.foxminded.university.service.exception.InvalidUserException;
import ua.foxminded.university.service.exception.UserNotFoundException;

@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean isAuthenticated(String username, String password) throws InvalidUserException {
        try {
            doAuthenticate(username, password);
            return true;
        } catch (UsernameNotFoundException e) {
            log.error("[ON isAuthenticated]:: invalid username");
            throw new UserNotFoundException(e.getMessage());
        } catch(AuthenticationException e) {
            log.error("[ON isAuthenticated]:: invalid credentials");
            throw new InvalidUserException(e.getMessage());
        }        
    }
    
    private void doAuthenticate(String username, String password) {
            log.info("[ON authenticate]:: authenticating by UsernamePasswordAuthenticationToken...");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info(
                    "[ON authenticate]:: user with username [ {} ] is authenticated by UsernamePasswordAuthenticationToken successfully",
                    username);
    }
}
