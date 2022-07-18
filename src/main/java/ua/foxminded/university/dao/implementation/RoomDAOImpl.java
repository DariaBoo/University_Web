package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.entities.Room;

@Repository
public class RoomDAOImpl implements RoomDAO {

    @Autowired
    private SessionFactory sessionFactory;    
    @Autowired
    private GroupDAOImpl groupDAOImpl;
    
    private static final String debugMessage = "Get current session - {}";
    private static final Logger log = LoggerFactory.getLogger(RoomDAOImpl.class.getName());

    @Override
    public Optional<List<Room>> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Room>> result = Optional.ofNullable(currentSession.createQuery("from Room", Room.class).getResultList());
        log.debug("Find all holidays from the database and return optionl list of rooms - {}", result);
        return result;
    }
    
    @Override
    public Optional<List<Room>> findSuitableRooms(int groupID){
        int countOfStudents = groupDAOImpl.getCountOfStudents(groupID);
        Session currentSession = sessionFactory.getCurrentSession();
        log.info(debugMessage, currentSession);
        Optional<List<Room>> resultList = Optional.ofNullable(currentSession.createNamedQuery("Room_FindByCapacity", Room.class)
                .setParameter("capacity", countOfStudents)
                .getResultList());
        log.debug("Take all rooms -{} suitable for student count", resultList);       
        return resultList;
    }
}
