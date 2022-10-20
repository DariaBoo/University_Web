package ua.foxminded.university.service.implementation;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.entities.Role;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleDAO roleDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Role findByName(String name) {
        Role role = roleDAO.findByName(name).orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.info("Found role {}", role);
        return role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role addRole(Role role) {
        log.info("Adding a new role {}", role);
        return roleDAO.save(role);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRole(int roleId) {
        log.info("Deleting role with id {}", roleId);
        roleDAO.deleteById(roleId);
    }
}
