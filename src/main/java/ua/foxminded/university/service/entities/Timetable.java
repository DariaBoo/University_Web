package ua.foxminded.university.service.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "timetable.timetable", uniqueConstraints = {
        @UniqueConstraint(name = "unique_group_date_time", columnNames = { "date", "time_period", "group_id" }),
        @UniqueConstraint(name = "unique_teacher_date_time", columnNames = { "date", "time_period", "teacher_id" }),
        @UniqueConstraint(name = "unique_room_date_time", columnNames = { "date", "time_period", "room_id" }) })
@NamedQueries({
        @NamedQuery(name = "Timetable_FindByPeriod", query = "FROM Timetable t WHERE t.date >= :dateStart AND t.date <= :dateEnd"),
        @NamedQuery(name = "Timetable_FindByTeacherAndDate", query = "FROM Timetable t WHERE t.teacher = :teacher AND t.date >= :dateStart AND t.date <= :dateEnd"),
        @NamedQuery(name = "Timetable_FindByGroupAndDate", query = "FROM Timetable t WHERE t.group = :group AND t.date >= :dateStart AND t.date <= :dateEnd") })
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int timetableId;

    @Column(name = "date", table = "timetable.timetable")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "time_period", length = 13, table = "timetable.timetable")
    private String lessonTimePeriod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
