package ua.foxminded.university.service.implementation;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.RoleDAO;
import ua.foxminded.university.service.RoleService;
import ua.foxminded.university.service.entities.Role;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleDAO roleDAO;

    @Override
    public Role findByName(String name) {
        return roleDAO.findByName(name).orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    @Override
    public void addRole(Role role) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteRole(int roleId) {
        // TODO Auto-generated method stub
        
    }

}
