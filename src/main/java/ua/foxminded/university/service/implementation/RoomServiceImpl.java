package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.entities.Room;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDAO roomDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Room> findAll() {
        List<Room> resultList = roomDAO.findAll().orElseThrow(() -> new IllegalArgumentException("Error occured while searching all groups"));
        log.debug("Return list of rooms - {}", resultList);
        return resultList;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Room> findSuitableRooms(int groupID) {
        List<Room> resultList = roomDAO.findSuitableRooms(groupID).orElseThrow(() -> new IllegalArgumentException("Error occured searching suitable groups"));
        log.debug("Return list of rooms - {}", resultList);
        return resultList;
    }
}
