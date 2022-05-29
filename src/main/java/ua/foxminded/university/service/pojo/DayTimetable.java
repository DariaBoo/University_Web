package ua.foxminded.university.service.pojo;

public class DayTimetable {
    private Day day;
    private Group group;
    private int roomNumber;
    private Teacher teacher;
    private Lesson lesson;

    private DayTimetable(Day day, Group group, int roomNumber, Teacher teacher,
            Lesson lesson) {
        this.day = day;
        this.group = group;
        this.roomNumber = roomNumber;
        this.teacher = teacher;
        this.lesson = lesson;
    }

    public static TimetableBuilder builder() {
        return new TimetableBuilder();
    }

    public static class TimetableBuilder {
        private Day day;
        private Group group;
        private int roomNumber;
        private Teacher teacher;
        private Lesson lesson;

        public TimetableBuilder setDay(Day day) {
            this.day = day;
            return this;
        }
       
        public TimetableBuilder setGroup(Group group) {
            this.group = group;
            return this;
        }

        public TimetableBuilder setRoomNumber(int roomNumber) {
            this.roomNumber = roomNumber;
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

        public DayTimetable buildWith(Object object) {
            return construct(object).build();
        }

        private TimetableBuilder construct(Object object) {
            return this;
        }

        public DayTimetable build() {
            return new DayTimetable(day, group, roomNumber, teacher, lesson);
        }
    }

    public Day getDay() {
        return day;
    }

    public Group getGroup() {
        return group;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Lesson getLesson() {
        return lesson;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((lesson == null) ? 0 : lesson.hashCode());
        result = prime * result + roomNumber;
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DayTimetable other = (DayTimetable) obj;
        if (day == null) {
            if (other.day != null)
                return false;
        } else if (!day.equals(other.day))
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
        if (roomNumber != other.roomNumber)
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }   
}
