package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.foxminded.university.dao.implementation.HolidayDAOImpl;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.pojo.Holiday;

@Service
public class HolidayServiceImpl implements HolidayService{
    private final HolidayDAOImpl holidayDAOImpl;
    
    @Autowired
    public HolidayServiceImpl(HolidayDAOImpl holidayDAOImpl) {
        this.holidayDAOImpl = holidayDAOImpl;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Holiday> findAllHolidays() {
        return holidayDAOImpl.findAllHolidays().orElseThrow(() -> new IllegalArgumentException("Error occured"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int addHoliday(Holiday holiday) {
        return holidayDAOImpl.addHoliday(holiday);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteHoliday(int holidayID) {
        int result = holidayDAOImpl.deleteHoliday(holidayID);
        System.out.println(result);
        return result;
    }

}
