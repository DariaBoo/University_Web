package ua.foxminded.university.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "timetable.students", uniqueConstraints = { @UniqueConstraint(name = "unique_name_surname_idCard", columnNames = {"first_name", "last_name", "id_card"}),
        @UniqueConstraint(name = "unique_group", columnNames = {"first_name", "last_name", "id_card", "group_id"})})
@org.hibernate.annotations.NamedQuery(name = "Student_changePassword", query = "UPDATE Student s SET s.password = :newPassword WHERE s.id = :id")
@DynamicUpdate
public class Student extends User {

    @Column(name = "id_card", unique = true)
    private String idCard;
    
    @ManyToOne(targetEntity = Group.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false) 
    private Group group;      

    public Student() {

    }

    public Student(int id, String firstName, String lastName, String password, String idCard,
            Group group) {
        super(id, firstName, lastName, password);
        this.idCard = idCard;
        this.group = group;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private int id;
        private String firstName;
        private String lastName;
        private String password;
        private String idCard;
        private Group group;

        public StudentBuilder setID(int studentID) {
            id = studentID;
            return this;
        }

        public StudentBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public StudentBuilder setIdCard(String idCard) {
            this.idCard = idCard;
            return this;
        }

        public StudentBuilder setGroup(Group group) {
            this.group = group;
            return this;
        }

        public Student buildWith(Object object) {
            return construct(object).build();
        }

        private StudentBuilder construct(Object object) {
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName, password, idCard, group);
        }
    }

    public String getIdCard() {
        return idCard;
    }

    public Group getGroup() {
        return group;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public int getGroupID() {
        return this.group.getId();
    }

    @Override
    public int hashCode() {
        return 14;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (idCard == null) {
            if (other.idCard != null)
                return false;
        } else if (!idCard.equals(other.idCard))
            return false;
        return true;
    } 
}
