package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.RoomDAOImpl;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.pojo.Room;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomDAOImpl roomDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Autowired
    public RoomServiceImpl(RoomDAOImpl roomDAOImpl) {
        this.roomDAOImpl = roomDAOImpl;
    }

    @Override
    public List<Room> findAll() {
        List<Room> resultList = roomDAOImpl.findAll().orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Return list of rooms - {}", resultList);
        return resultList;
    }
}
