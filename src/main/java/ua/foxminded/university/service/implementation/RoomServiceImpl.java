package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.RoomDAOImpl;
import ua.foxminded.university.service.RoomService;
import ua.foxminded.university.service.pojo.Room;

@Service
public class RoomServiceImpl implements RoomService{
    private final RoomDAOImpl roomDAOImpl;
    
    @Autowired
    public RoomServiceImpl(RoomDAOImpl roomDAOImpl) {
        this.roomDAOImpl = roomDAOImpl;
    }

    @Override
    public List<Room> findAll() {
        return roomDAOImpl.findAll();
    }

}
