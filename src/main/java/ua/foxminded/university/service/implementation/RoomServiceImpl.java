package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    @Transactional(readOnly = true)
    @Cacheable("rooms")
    public List<Room> findAll() {
        List<Room> resultList = roomDAO.findAll();
        log.debug("Found all rooms, list size :: {}", resultList.size());
        return resultList;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Room> findSuitableRooms(int capacity) {
        List<Room> resultList = roomDAO.findSuitableRooms(capacity).orElseThrow(() -> new IllegalArgumentException("Error occured searching suitable groups"));
        log.debug("Found list of suitable rooms (count - {}) by capacity :: {}", resultList.size(), capacity);
        return resultList;
    }
}
