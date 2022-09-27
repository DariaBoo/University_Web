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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"student", "teacher", "staff"})
@Getter
@Setter
@EqualsAndHashCode(exclude = {"student", "teacher", "staff"})
@Entity
@Table(name = "users",  uniqueConstraints = { @UniqueConstraint(name = "unique_name_surname_username", columnNames = {"user_name", "first_name", "last_name"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    
    @NotBlank(message = "User name may not be blank")
    @Column(name = "user_name", unique = true)
    private String username;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    
    @NotBlank(message = "First name may not be blank")
    @Size(max = 30)
    @Column(name = "first_name")
    private String firstName;
    
    @NotBlank(message = "Last name may not be blank")
    @Size(max = 30)
    @Column(name = "last_name")
    private String lastName;
    
    @JsonIgnore
    @Column(name = "password", length = 255)
    private String password;
    
    @OneToOne(mappedBy = "user")
    private Student student;
    
    @OneToOne(mappedBy = "user")
    private Teacher teacher;
    
    @OneToOne(mappedBy = "user")
    private Staff staff;
}
