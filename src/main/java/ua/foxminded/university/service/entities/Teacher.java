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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @Generated(GenerationTime.INSERT)
    @ColumnDefault(value = "'555'")
    @Column(name = "password", length = 10)
    private String password;
    
    @NotBlank(message = "Position may not be blank")
    @Size(max = 30, message = "Position must be equals or less than 30 characters long")
    @Column(name = "position")
    private String position;
    
    @Column(name = "department_id")
    private int departmentId;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher")
    private List<Day> absentPeriod;
    
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) 
    @JoinTable(
            name = "lessons_teachers", 
            joinColumns = { @JoinColumn(name = "teacher_id", referencedColumnName = "id") }, 
            inverseJoinColumns = { @JoinColumn(name = "lesson_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "teacher_id",
            "lesson_id" }))
    private List<Lesson> lessons;
    
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
