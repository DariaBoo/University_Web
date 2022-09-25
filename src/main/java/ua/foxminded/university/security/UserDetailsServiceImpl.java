package ua.foxminded.university.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.StaffDAO;
import ua.foxminded.university.security.model.AuthorisedUser;
import ua.foxminded.university.service.entities.Staff;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//    @Autowired
//    private StudentDAO studentDAO;
//    @Autowired
//    private TeacherDAO teacherDAO;
    @Autowired
    private StaffDAO staffDAO;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Staff user = staffDAO.findByUsername(username);
        log.info("LOGIN AS ADMIN/USER-------------------------------------------");
        if (user == null) {
            throw new UsernameNotFoundException("User not found for username" + username);
        }
        log.info("ADMIN/USER ROLE------------------------------" + user.getRoles().get(0).getName());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getRoles());
//        return new AuthorisedUser(user);
    }
    
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = studentDAO.findByUsername(username);
//        log.info("ATTEMPT TO LOGIN WITH USERNAME - {}", username);
//        log.info("ATTEMPT TO LOGIN AS STUDENT");
//        if(user == null) {
//            user = teacherDAO.findByUsername(username);
//            log.info("ATTEMPT TO LOGIN AS TEACHER");
//            if(user == null) {
//                user = staffDAO.findByUsername(username);
//                log.info("ATTEMPT TO LOGIN AS STAFF");
//                if(user == null) {
//                    log.warn("WRONG USERNAME OR PASSWORD");
//                    throw new UsernameNotFoundException("No user with username " + username);
//                }
//            }
//        }
//        return new AuthorisedUser(user);
//    }
}
