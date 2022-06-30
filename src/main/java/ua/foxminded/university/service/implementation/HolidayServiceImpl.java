package ua.foxminded.university.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.HolidayDAOImpl;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.pojo.Holiday;

@Service
public class HolidayServiceImpl implements HolidayService {
    
    private final HolidayDAOImpl holidayDAOImpl;
    private static final Logger log = LoggerFactory.getLogger(HolidayServiceImpl.class);

    @Autowired
    public HolidayServiceImpl(HolidayDAOImpl holidayDAOImpl) {
        this.holidayDAOImpl = holidayDAOImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Holiday> findAllHolidays() {
        List<Holiday> resultList = holidayDAOImpl.findAllHolidays().orElseThrow(() -> new IllegalArgumentException("Error occured"));
        log.debug("Take list of holidays - {}, otherwise return IllegalArgumentException", resultList);
        return resultList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addHoliday(Holiday holiday) {
        int result = holidayDAOImpl.addHoliday(holiday);
        log.debug("Add new holiday - {} and return a result - {}", holiday, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteHoliday(int holidayID) {
        int result = holidayDAOImpl.deleteHoliday(holidayID);
        log.debug("Delete holiday with id - {} and return a result - {}", holidayID, result);
        return result;
    }
}
