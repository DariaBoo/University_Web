package ua.foxminded.university.service.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Range;

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
    @Range(min = 1, message = "Room number may not be zero")
    @Column(name = "room_id")
    private int number;

    @Range(min = 10, max = 30, message = "Room capacity must be between 10 and 30 ")
    @Column(name = "capacity")
    private int capacity;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private List<Timetable> timetable;
}
