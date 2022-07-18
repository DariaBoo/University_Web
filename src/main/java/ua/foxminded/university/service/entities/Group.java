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

@NamedEntityGraphs({ @NamedEntityGraph(name = "graph.groupLessons", attributeNodes = @NamedAttributeNode("lessons")),
        @NamedEntityGraph(name = "graph.groupStudents", attributeNodes = @NamedAttributeNode("students")) })
@Entity
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

    public Group() {

    }

    public Group(int id, String name, int departmentID, Set<Student> students, Set<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.departmentID = departmentID;
        this.students = students;
        this.lessons = lessons;
    }

    public static GroupBuilder builder() {
        return new GroupBuilder();
    }

    public static class GroupBuilder {
        private int id;
        private String name;
        private int departmentID;
        private Set<Student> students;
        private Set<Lesson> lessons;

        public GroupBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public GroupBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GroupBuilder setDepartmentID(int departmentID) {
            this.departmentID = departmentID;
            return this;
        }

        public GroupBuilder setStudents(Set<Student> students) {
            this.students = students;
            return this;
        }

        public GroupBuilder setLessons(Set<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        public Group build() {
            return new Group(id, name, departmentID, students, lessons);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Set<Timetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(Set<Timetable> timetable) {
        this.timetable = timetable;
    }

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (departmentID != other.departmentID)
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
