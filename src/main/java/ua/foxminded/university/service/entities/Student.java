package ua.foxminded.university.service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = {"group"})
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
}
