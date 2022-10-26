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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "timetable", uniqueConstraints = {
        @UniqueConstraint(name = "unique_group_date_time", columnNames = { "date", "time_period", "group_id" }),
        @UniqueConstraint(name = "unique_teacher_date_time", columnNames = { "date", "time_period", "teacher_id" }),
        @UniqueConstraint(name = "unique_room_date_time", columnNames = { "date", "time_period", "room_id" }) })
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int timetableId;

    @NotNull(message = "Date may not be null")
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "Lesson time period may not be null")
    @Column(name = "time_period", length = 13)
    private String lessonTimePeriod;

    @NotNull(message = "Group may not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @NotNull(message = "Room may not be null")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull(message = "Teacher may not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @NotNull(message = "Lesson may not be null")
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
