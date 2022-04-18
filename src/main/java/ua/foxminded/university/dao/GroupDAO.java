package ua.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import ua.foxminded.university.dao.exception.*;
import ua.foxminded.university.service.pojo.*;

public interface GroupDAO {

    public Optional<List<Group>> selectBySize(int groupSize) throws DAOException;

    public void addGroup(Group group);

    public void deleteGroup(int groupID);

}
