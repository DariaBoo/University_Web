package ua.foxminded.university.dao.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(RoomDAOImpl.class.getName());
    
    @Autowired
    public RoomDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Optional<List<Room>> findAll() {
        log.trace("Find all holidays from the database");
        Optional<List<Room>> result = Optional.of(jdbcTemplate.query(FIND_ALL_ROOMS, new RoomMapper()));
        log.debug("Return optionl list of rooms - {}", result);
        return result;
    }    
}
