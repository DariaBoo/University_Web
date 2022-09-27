package ua.foxminded.university.service;

import ua.foxminded.university.service.entities.Role;

public interface RoleService {

    Role findByName(String name);
    
    void addRole(Role role);
    
    void deleteRole(int roleId);
}
