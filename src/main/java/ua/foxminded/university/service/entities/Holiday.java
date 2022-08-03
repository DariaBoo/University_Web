package ua.foxminded.university.service.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@NoArgsConstructor
@Table(name = "holidays")
@EqualsAndHashCode
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "Date may not be null.")
    @Column(name = "date", unique = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @NotBlank(message = "Holiday name may not be blank.")
    @Size(max = 20, message = "Holiday name must be equals or less then 20 characters long")
    @Column(name = "holiday")
    private String name;
}
