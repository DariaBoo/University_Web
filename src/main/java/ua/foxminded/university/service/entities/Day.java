package ua.foxminded.university.service.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "timetable.teacherAbsent", uniqueConstraints = { @UniqueConstraint(name = "unique_dates", columnNames = {"teacher_id", "date_start", "date_end"})})
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "date_start", table = "timetable.teacherAbsent")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOne;

    @Column(name = "date_end", table = "timetable.teacherAbsent")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTwo;
    
    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id",  referencedColumnName = "id")
    private Teacher teacher;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public int hashCode() {
        return 16;
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
        if (id != other.id)
            return false;
        return true;
    }
}
