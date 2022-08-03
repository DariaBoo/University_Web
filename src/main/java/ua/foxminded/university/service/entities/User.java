package ua.foxminded.university.service.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    
    @NotBlank(message = "First name may not be blank")
    @Size(max = 30)
    @Column(name = "first_name")
    private String firstName;
    
    @NotBlank(message = "Last name may not be blank")
    @Size(max = 30)
    @Column(name = "last_name")
    private String lastName;
}
