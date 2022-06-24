package ua.foxminded.university.service.pojo;

import java.time.LocalDate;

public class Holiday {
    private int id;
    private LocalDate date;
    private String holiday;
    
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getHolidayName() {
        return holiday;
    }
    public void setHolidayName(String holiday) {
        this.holiday = holiday;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((holiday == null) ? 0 : holiday.hashCode());
        result = prime * result + id;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Holiday other = (Holiday) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (holiday == null) {
            if (other.holiday != null)
                return false;
        } else if (!holiday.equals(other.holiday))
            return false;
        if (id != other.id)
            return false;
        return true;
    }    
}
