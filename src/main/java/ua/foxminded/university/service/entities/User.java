package ua.foxminded.university.service.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    
    @NotBlank(message = "User name may not be blank")
    @Column(name = "user_name", unique = true)
    private String username;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "staff_roles", joinColumns = @JoinColumn(name = "staff_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private List<Role> roles;
    
    @NotBlank(message = "First name may not be blank")
    @Size(max = 30)
    @Column(name = "first_name")
    private String firstName;
    
    @NotBlank(message = "Last name may not be blank")
    @Size(max = 30)
    @Column(name = "last_name")
    private String lastName;
    
    @JsonIgnore
    @Column(name = "password", length = 10)
    private String password;
}
