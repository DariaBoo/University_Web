package ua.foxminded.university.service.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Entity
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"absentPeriod", "lessons", "timetable" })
@Table(name = "teachers", uniqueConstraints = { @UniqueConstraint(name = "unique_name_surname", columnNames = {"first_name", "last_name"})})
@DynamicUpdate
public class Teacher extends User {

    @Column(name = "position", length = 30, nullable = false)
    private String position;
    
    @Column(name = "department_id")
    private int departmentId;

    @OneToMany(mappedBy = "teacher")
    private List<Day> absentPeriod;
    
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) 
    @JoinTable(
            name = "lessons_teachers", 
            joinColumns = { @JoinColumn(name = "teacher_id", referencedColumnName = "id") }, 
            inverseJoinColumns = { @JoinColumn(name = "lesson_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "teacher_id",
            "lesson_id" }))
    private List<Lesson> lessons;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
