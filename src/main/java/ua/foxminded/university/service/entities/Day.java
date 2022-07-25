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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"teacher"})
@Table(name = "timetable.teacherAbsent", uniqueConstraints = { @UniqueConstraint(name = "unique_dates", columnNames = {"teacher_id", "date_start", "date_end"})})
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NonNull
    @Column(name = "date_start", table = "timetable.teacherAbsent")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOne;

    @NonNull
    @Column(name = "date_end", table = "timetable.teacherAbsent")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTwo;
    
    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id",  referencedColumnName = "id")
    private Teacher teacher;  
}
