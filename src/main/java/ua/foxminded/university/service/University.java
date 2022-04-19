package ua.foxminded.university.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ua.foxminded.university.service.pojo.DayTimetable;
import ua.foxminded.university.service.pojo.Department;

public class University {
    private List<Department> departments;

    public void createTimetable() {

    }

    public DayTimetable getDayTimetable(LocalDate date) {
        return new DayTimetable.TimetableBuilder().build();
    }

    public Map<LocalDate, DayTimetable> getMonthTimetable(LocalDate startDate, LocalDate endDate) {
        return new LinkedHashMap<LocalDate, DayTimetable>();
    }
}
