package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.entities.Room;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDAO roomDAO;
    
    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Override
    @Transactional
    public List<Room> findAll() {
        List<Room> resultList = roomDAO.findAll().orElseThrow(() -> new IllegalArgumentException("Error occured while searching all groups"));
        log.debug("Return list of rooms - {}", resultList);
        return resultList;
    }
    
    @Override
    @Transactional
    public List<Room> findSuitableRooms(int groupID) {
        List<Room> resultList = roomDAO.findSuitableRooms(groupID).orElseThrow(() -> new IllegalArgumentException("Error occured searching suitable groups"));
        log.debug("Return list of rooms - {}", resultList);
        return resultList;
    }
}
