package ua.foxminded.university.service.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NamedEntityGraph(name = "graph.teacherLessons", attributeNodes = @NamedAttributeNode("lessons"))
@Entity
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"absentPeriod", "lessons", "timetable" })
@Table(name = "timetable.teachers", uniqueConstraints = { @UniqueConstraint(name = "unique_name_surname", columnNames = {"first_name", "last_name"})})
@NamedQuery(name = "Teacher_changePassword", query = "UPDATE Teacher t SET t.password = :newPassword WHERE t.id = :id")
@DynamicUpdate
public class Teacher extends User {

    @Column(name = "position", length = 30, nullable = false)
    private String position;
    
    @Column(name = "department_id")
    private int departmentID;

    @OneToMany(mappedBy = "teacher")
    private Set<Day> absentPeriod;
        
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) 
    @JoinTable(
            name = "timetable.lessons_teachers", 
            joinColumns = { @JoinColumn(name = "teacher_id", referencedColumnName = "id") }, 
            inverseJoinColumns = { @JoinColumn(name = "lesson_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "teacher_id",
            "lesson_id" }))
    private Set<Lesson> lessons = new HashSet<>();
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private Set<Timetable> timetable;
}
