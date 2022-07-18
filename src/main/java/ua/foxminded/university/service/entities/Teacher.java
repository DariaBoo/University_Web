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

@NamedEntityGraph(name = "graph.teacherLessons", attributeNodes = @NamedAttributeNode("lessons"))
@Entity
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

    public Teacher() {

    }

    public Teacher(int id, String firstName, String lastName, String password, String position, int departmentID, Set<Day> absentPeriod, Set<Lesson> lessons) {
        super(id, firstName, lastName, password);
        this.departmentID = departmentID;
        this.position = position;
        this.absentPeriod = absentPeriod;
        this.lessons = lessons;
    }

    public static TeacherBuidler builder() {
        return new TeacherBuidler();
    }

    public static class TeacherBuidler {
        private int id;
        private String firstName;
        private String lastName;
        private String password;
        private String position;
        private int departmentID;
        private Set<Day> absentPeriod;
        private Set<Lesson> lessons;

        public TeacherBuidler setID(int id) {
            this.id = id;
            return this;
        }

        public TeacherBuidler setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public TeacherBuidler setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public TeacherBuidler setPassword(String password) {
            this.password = password;
            return this;
        }

        public TeacherBuidler setPosition(String position) {
            this.position = position;
            return this;
        }

        public TeacherBuidler setDepartmentID(int departmentID) {
            this.departmentID = departmentID;
            return this;
        }

        public TeacherBuidler setAbsentPeriod(Set<Day> absentPeriod) {
            this.absentPeriod = absentPeriod;
            return this;
        }
        
        public TeacherBuidler setLessons(Set<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        private TeacherBuidler construct(Object object) {
            return this;
        }

        public Teacher build() {
            return new Teacher(id, firstName, lastName, password, position, departmentID, absentPeriod, lessons);
        }

        public Teacher buildWith(Object object) {
            return construct(object).build();
        }
    }

    public String getPosition() {
        return position;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public Set<Day> getAbsentPeriod() {
        return absentPeriod;
    }

    public void setAbsentPeriod(Set<Day> absentPeriod) {
        this.absentPeriod = absentPeriod;
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
        return 10;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        if (departmentID != other.departmentID)
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }
}
