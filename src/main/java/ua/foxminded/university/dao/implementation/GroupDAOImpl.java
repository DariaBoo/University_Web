package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.exception.DAOException;
import ua.foxminded.university.service.pojo.Group;

public class GroupDAOImpl implements GroupDAO {

    @Override
    public Optional<List<Group>> selectBySize(int groupSize) throws DAOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addGroup(Group group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteGroup(int groupID) {
        // TODO Auto-generated method stub

    }

}
