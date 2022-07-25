package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.GroupDAO;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.entities.Room;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Repository
public class RoomDAOImpl implements RoomDAO {

    @Autowired
    private SessionFactory sessionFactory;    
    @Autowired
    private GroupDAO groupDAO;
    
    private static final String debugMessage = "Get current session - {}";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Room>> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Room>> result = Optional.ofNullable(currentSession.createQuery("from Room", Room.class).getResultList());
        log.debug("Find all holidays from the database and return optionl list of rooms - {}", result);
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<List<Room>> findSuitableRooms(int groupID){
        int countOfStudents = groupDAO.getCountOfStudents(groupID);
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Room>> resultList = Optional.ofNullable(currentSession.createNamedQuery("Room_FindByCapacity", Room.class)
                .setParameter("capacity", countOfStudents)
                .getResultList());
        log.debug("Take all rooms -{} suitable for student count", resultList);       
        return resultList;
    }
}
