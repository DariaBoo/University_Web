package ua.foxminded.university.service.pojo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class Day {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOne;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTwo;
    private String lessonTimePeriod;

    public Day(LocalDate dateOne, LocalDate dateTwo) {
        this.dateOne = dateOne;
        this.dateTwo = dateTwo;
    }

    public Day() {

    }

    public LocalDate getDateOne() {
        return dateOne;
    }

    public void setDateOne(LocalDate dateOne) {
        this.dateOne = dateOne;
    }

    public LocalDate getDateTwo() {
        return dateTwo;
    }

    public void setDateTwo(LocalDate dateTwo) {
        this.dateTwo = dateTwo;
    }

    public String getLessonTimePeriod() {
        return lessonTimePeriod;
    }

    public void setLessonTimePeriod(String lessonTimePeriod) {
        this.lessonTimePeriod = lessonTimePeriod;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateOne == null) ? 0 : dateOne.hashCode());
        result = prime * result + ((dateTwo == null) ? 0 : dateTwo.hashCode());
        result = prime * result + ((lessonTimePeriod == null) ? 0 : lessonTimePeriod.hashCode());
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
        Day other = (Day) obj;
        if (dateOne == null) {
            if (other.dateOne != null)
                return false;
        } else if (!dateOne.equals(other.dateOne))
            return false;
        if (dateTwo == null) {
            if (other.dateTwo != null)
                return false;
        } else if (!dateTwo.equals(other.dateTwo))
            return false;
        if (lessonTimePeriod == null) {
            if (other.lessonTimePeriod != null)
                return false;
        } else if (!lessonTimePeriod.equals(other.lessonTimePeriod))
            return false;
        return true;
    }
}
