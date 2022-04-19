package ua.foxminded.university.dao;

import ua.foxminded.university.service.pojo.DayTimetable;

public interface TimetableDAO {

    public void setTimetable(DayTimetable timetable);

    public void setRoom();

    public void setTeacher();

}
