package ua.foxminded.university.service.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
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
@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users",  uniqueConstraints = { @UniqueConstraint(name = "unique_name_surname_username", columnNames = {"user_name", "first_name", "last_name"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    
    @NotBlank(message = "Username may not be blank")
    @Size(max = 30)
    @Column(name = "user_name", unique = true)
    private String username;
    
    @ManyToMany(fetch = FetchType.EAGER)
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
}
