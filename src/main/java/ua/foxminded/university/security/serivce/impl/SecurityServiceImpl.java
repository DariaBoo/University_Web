package ua.foxminded.university.security.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.security.service.SecurityService;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated(String username, String password) {
        try {
            doAuthenticate(username, password);
            return true;
        } catch (AuthenticationException e) {
            log.error("[ON isAuthenticated]:: Exception :: {}.", e);
            throw new BadCredentialsException(e.getMessage());
        }
    }
    
    private void doAuthenticate(String username, String password) {
            log.info("[ON authenticate]:: authenticating by UsernamePasswordAuthenticationToken...");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info(
                    "[ON authenticate]:: user with username [ {} ] is authenticated successfully",
                    username);
    }
}
