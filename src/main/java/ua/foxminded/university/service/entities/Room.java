package ua.foxminded.university.service.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "timetable.rooms")
@NamedQuery(name = "Room_FindByCapacity", query = "FROM Room r WHERE r.capacity >= :capacity")
public class Room {

    @Id
    @Column(name = "room_id")
    private int number;
    
    @Column(name = "capacity")
    private int capacity;
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, targetEntity = Timetable.class)
    private Set<Timetable> timetable;
    

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<Timetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(Set<Timetable> timetable) {
        this.timetable = timetable;
    }

    @Override
    public int hashCode() {
        return 11;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        if (capacity != other.capacity)
            return false;
        if (number != other.number)
            return false;
        if (timetable == null) {
            if (other.timetable != null)
                return false;
        } else if (!timetable.equals(other.timetable))
            return false;
        return true;
    }
}
