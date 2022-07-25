package ua.foxminded.university.service.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraph(name = "graph.lesson-groups-teachers", attributeNodes = { @NamedAttributeNode("groups"), @NamedAttributeNode("teachers")})
@Entity
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"teachers", "groups", "timetable" })
@Table(name = "timetable.lessons")
@NamedQuery(name = "Lesson.findLessonsByTeacherId", query = "SELECT l.id, l.name, l.description FROM Lesson AS l LEFT JOIN l.teachers AS t WHERE t.id = :teacherID")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private int id;

    @Column(name = "lesson_name", length = 20, unique = true)
    private String name;

    @Column(name = "description", length = 100)
    private String description;
    
    @ManyToMany(mappedBy = "lessons")
    private Set<Teacher> teachers = new HashSet<>();
    
    @ManyToMany(mappedBy = "lessons")
    private Set<Group> groups = new HashSet<>();
    
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private Set<Timetable> timetable;
}
