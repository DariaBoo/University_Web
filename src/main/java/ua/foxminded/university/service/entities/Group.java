package ua.foxminded.university.service.entities;

import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraphs({ @NamedEntityGraph(name = "graph.groupLessons", attributeNodes = @NamedAttributeNode("lessons")),
        @NamedEntityGraph(name = "graph.groupStudents", attributeNodes = @NamedAttributeNode("students")) })
@Entity
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"students", "lessons", "timetable"})
@Table(name = "timetable.groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int id;

    @Column(name = "group_name", length = 5, unique = true)
    private String name;

    @Column(name = "department_id")
    private int departmentID;

    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "timetable.groups_lessons", joinColumns = {
            @JoinColumn(name = "group_id") }, inverseJoinColumns = {
                    @JoinColumn(name = "lesson_id") }, uniqueConstraints = @UniqueConstraint(columnNames = { "group_id",
                            "lesson_id" }))
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private Set<Timetable> timetable;
}
