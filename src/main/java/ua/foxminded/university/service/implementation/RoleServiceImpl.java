package ua.foxminded.university.service.implementation;

import javax.persistence.EntityNotFoundException;
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
        Role role = roleDAO.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("No role with name " + name));
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
        if (roleDAO.existsById(roleId)) {
            log.info("Deleting role with id {}", roleId);
            roleDAO.deleteById(roleId);
        } else {
            log.warn("Role with id {} doesn't exist. Nothing to delete.", roleId);
            throw new EntityNotFoundException("Role with id " + roleId + " doesn't exist. Nothing to delete.");
        }
    }
}
