package ua.foxminded.university.service.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "absentPeriod", "lessons", "timetable" })
@Table(name = "teachers")
@DynamicUpdate
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Position may not be blank")
    @Size(max = 30, message = "Position must be equals or less than 30 characters long")
    @Column(name = "position")
    private String position;

    @Column(name = "department_id")
    private int departmentId;

    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Day> absentPeriod = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "lessons_teachers", joinColumns = {
            @JoinColumn(name = "teacher_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "lesson_id") }, uniqueConstraints = @UniqueConstraint(columnNames = {
                            "teacher_id", "lesson_id" }))
    private List<Lesson> lessons = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
