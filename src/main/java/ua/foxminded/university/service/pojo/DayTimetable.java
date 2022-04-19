package ua.foxminded.university.service.pojo;

import java.time.LocalDate;

public class DayTimetable {
    private LocalDate date;
    private String lessonTimePeriod;
    private int groupID;
    private int roomNumber;
    private Teacher teacher;
    private String lesson;

    private DayTimetable(LocalDate date, String lessonTimePeriod, int groupID, int roomNumber, Teacher teacher,
            String lesson) {
        this.date = date;
        this.lessonTimePeriod = lessonTimePeriod;
        this.groupID = groupID;
        this.roomNumber = roomNumber;
        this.teacher = teacher;
        this.lesson = lesson;
    }

    public static TimetableBuilder builder() {
        return new TimetableBuilder();
    }

    public static class TimetableBuilder {
        private LocalDate date;
        private String lessonTimePeriod;
        private int groupID;
        private int roomNumber;
        private Teacher teacher;
        private String lesson;

        public TimetableBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public TimetableBuilder setLessonTimePeriod(String lessonTimePeriod) {
            this.lessonTimePeriod = lessonTimePeriod;
            return this;
        }

        public TimetableBuilder setGroupID(int groupID) {
            this.groupID = groupID;
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

        public TimetableBuilder setLesson(String lesson) {
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
            return new DayTimetable(date, lessonTimePeriod, groupID, roomNumber, teacher, lesson);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLessonTimePeriod() {
        return lessonTimePeriod;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getLesson() {
        return lesson;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + groupID;
        result = prime * result + ((lesson == null) ? 0 : lesson.hashCode());
        result = prime * result + ((lessonTimePeriod == null) ? 0 : lessonTimePeriod.hashCode());
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
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (groupID != other.groupID)
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
