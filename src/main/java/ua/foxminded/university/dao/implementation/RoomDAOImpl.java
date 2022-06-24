package ua.foxminded.university.dao.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.foxminded.university.dao.RoomDAO;
import ua.foxminded.university.dao.implementation.mappers.RoomMapper;
import ua.foxminded.university.service.pojo.Room;

@Repository
public class RoomDAOImpl implements RoomDAO{
    private final JdbcTemplate jdbcTemplate;
    private final String FIND_ALL_ROOMS = "SELECT * FROM timetable.rooms ORDER BY room_id";
    
    @Autowired
    public RoomDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public List<Room> findAll() {
        return jdbcTemplate.query(FIND_ALL_ROOMS, new RoomMapper());
    }
    

}
