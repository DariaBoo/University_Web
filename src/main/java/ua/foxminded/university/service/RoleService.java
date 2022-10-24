package ua.foxminded.university.service;

import ua.foxminded.university.service.entities.Role;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
public interface RoleService {

    /**
     * The method searching role by role's name
     * @param name
     * @return role
     */
    Role findByName(String name);
    
    /**
     * The method adding a new role to the database
     * @param role
     * @return added role
     */
    Role addRole(Role role);
    
    /**
     * The method delete role by role id
     * @param roleId
     */
    void deleteRole(int roleId);
}
