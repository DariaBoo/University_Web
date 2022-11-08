package ua.foxminded.university.service.entities;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = { "students", "lessons", "timetable" })
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int id;

    @NotBlank(message = "Group name may not be blank")
    @Size(max = 5, message = "Group name must be equals or less than 5 characters long")
    @Pattern(regexp = "(\\S+)-(\\d+)", message = "Group name should contain 2 characters, hyphen, 2 numbers")
    @Column(name = "group_name", unique = true)
    private String name;

    @Column(name = "department_id")
    private int departmentId;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Student> students;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groups_lessons", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = {
            @JoinColumn(name = "lesson_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "group_id",
                    "lesson_id" }))
    private List<Lesson> lessons;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
