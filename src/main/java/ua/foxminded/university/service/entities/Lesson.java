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

@NamedEntityGraph(name = "graph.lesson-groups-teachers", attributeNodes = { @NamedAttributeNode("groups"), @NamedAttributeNode("teachers")})
@Entity
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

    public Lesson() {

    }

    public Lesson(int id, String name, String description, Set<Teacher> teachers, Set<Group> groups) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teachers = teachers;
        this.groups = groups;
    }

    public static LessonBuilder builder() {
        return new LessonBuilder();
    }

    public static class LessonBuilder {
        private int id;
        private String name;
        private String description;
        private Set<Teacher> teachers;
        private Set<Group> groups;

        public LessonBuilder setID(int id) {
            this.id = id;
            return this;
        }

        public LessonBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public LessonBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public LessonBuilder setTeachers(Set<Teacher> teachers) {
            this.teachers = teachers;
            return this;
        }
        
        public LessonBuilder setGroups(Set<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Lesson build() {
            return new Lesson(id, name, description, teachers, groups);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    
    public Group getGroup(Group group) {
        return this.groups.stream().filter(g -> group.equals(g)).findAny().orElse(new Group());
    }

    public Set<Timetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(Set<Timetable> timetable) {
        this.timetable = timetable;
    }

    @Override
    public int hashCode() {
        return 12;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lesson other = (Lesson) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
