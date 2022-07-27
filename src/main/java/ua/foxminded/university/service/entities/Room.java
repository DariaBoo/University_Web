package ua.foxminded.university.service.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "timetable" })
@Table(name = "rooms")
public class Room {

    @Id
    @Column(name = "room_id")
    private int number;
    
    @Column(name = "capacity")
    private int capacity;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private Set<Timetable> timetable;
}
