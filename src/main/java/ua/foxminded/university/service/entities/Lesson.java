package ua.foxminded.university.service.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@EqualsAndHashCode(exclude = {"teachers", "groups", "timetable" })
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private int id;

    @Column(name = "lesson_name", length = 20, unique = true)
    private String name;

    @Column(name = "description", length = 100)
    private String description;
    
    @ToString.Exclude
    @ManyToMany(mappedBy = "lessons")
    private List<Teacher> teachers;
    
    @ToString.Exclude
    @ManyToMany(mappedBy = "lessons")
    private List<Group> groups;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
