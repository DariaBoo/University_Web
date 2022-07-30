package ua.foxminded.university.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.dao.exception.UniqueConstraintViolationException;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;

/**
 * @version 1.0
 * @author Bogush Daria
 *
 */
@Slf4j
@Service
public class HolidayServiceImpl implements HolidayService {    

    @Autowired
    private HolidayDAO holidayDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable("holidays")
    public List<Holiday> findAllHolidays() {
        List<Holiday> resultList = holidayDAO.findAll();
        log.debug("Found list of holidays, list size :: ", resultList.size());
        return resultList;
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    @Transactional
    public boolean addHoliday(Holiday holiday) {
        try {
            holidayDAO.save(holiday);
            log.debug("Add a new holiday");
        } catch(org.springframework.dao.DataIntegrityViolationException e) {
            log.error(e.getMessage(), e.getCause());
            throw new UniqueConstraintViolationException("Holiday with date :: " + holiday.getDate() + " already exists!");
        }
        return holidayDAO.existsById(holiday.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean deleteHoliday(int holidayId) {
        holidayDAO.deleteById(holidayId);
        return !holidayDAO.existsById(holidayId);
    }
}
