package ua.foxminded.university.service.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter 
@Setter 
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "timetable" })
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @NotBlank(message = "Room number may not be blank")
    @Column(name = "room_id")
    private int number;
    
    @NotBlank(message = "Room capacity may not be blank")
    @Size(min = 10, max = 30)
    @Column(name = "capacity")
    private int capacity;
    
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
