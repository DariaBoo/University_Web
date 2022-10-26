package ua.foxminded.university.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.university.dao.HolidayDAO;
import ua.foxminded.university.service.HolidayService;
import ua.foxminded.university.service.entities.Holiday;
import ua.foxminded.university.service.exception.UniqueConstraintViolationException;

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
     */
    @Override
    @Transactional
    public Holiday addHoliday(Holiday holiday) {
        Optional<Holiday> savedHoliday = holidayDAO.findByDate(holiday.getDate());
        if(savedHoliday.isPresent()) {
            log.warn("Holiday with date :: {} already exists!", holiday.getDate());
            throw new UniqueConstraintViolationException("Holiday with date :: " + holiday.getDate() + " already exists!");
        }
        return holidayDAO.save(holiday);
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
