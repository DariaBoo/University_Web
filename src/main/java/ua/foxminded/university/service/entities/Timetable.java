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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
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

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public Timetable() {

    }

    private Timetable(int id, LocalDate date, String lessonTimePeriod, Group group, Room room, Teacher teacher,
            Lesson lesson) {
        this.timetableId = id;
        this.date = date;
        this.lessonTimePeriod = lessonTimePeriod;
        this.group = group;
        this.room = room;
        this.teacher = teacher;
        this.lesson = lesson;
    }

    public static TimetableBuilder builder() {
        return new TimetableBuilder();
    }

    public static class TimetableBuilder {
        private int id;
        private LocalDate date;
        private String lessonTimePeriod;
        private Group group;
        private Room room;
        private Teacher teacher;
        private Lesson lesson;

        public TimetableBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public TimetableBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public TimetableBuilder setLessonTimePeriod(String lessonTimePeriod) {
            this.lessonTimePeriod = lessonTimePeriod;
            return this;
        }

        public TimetableBuilder setGroup(Group group) {
            this.group = group;
            return this;
        }

        public TimetableBuilder setRoom(Room room) {
            this.room = room;
            return this;
        }

        public TimetableBuilder setTeacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public TimetableBuilder setLesson(Lesson lesson) {
            this.lesson = lesson;
            return this;
        }

        public Timetable buildWith(Object object) {
            return construct(object).build();
        }

        private TimetableBuilder construct(Object object) {
            return this;
        }

        public Timetable build() {
            return new Timetable(id, date, lessonTimePeriod, group, room, teacher, lesson);
        }
    }

    public int getId() {
        return timetableId;
    }

    public Group getGroup() {
        return group;
    }

    public Room getRoom() {
        return room;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setId(int id) {
        this.timetableId = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLessonTimePeriod() {
        return lessonTimePeriod;
    }

    public void setLessonTimePeriod(String lessonTimePeriod) {
        this.lessonTimePeriod = lessonTimePeriod;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Timetable other = (Timetable) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (lesson == null) {
            if (other.lesson != null)
                return false;
        } else if (!lesson.equals(other.lesson))
            return false;
        if (lessonTimePeriod == null) {
            if (other.lessonTimePeriod != null)
                return false;
        } else if (!lessonTimePeriod.equals(other.lessonTimePeriod))
            return false;
        if (room == null) {
            if (other.room != null)
                return false;
        } else if (!room.equals(other.room))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        if (timetableId != other.timetableId)
            return false;
        return true;
    }
}
