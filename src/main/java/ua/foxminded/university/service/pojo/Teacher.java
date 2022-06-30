package ua.foxminded.university.service.pojo;

import java.util.List;

public class Teacher extends User {

    private String position;
    private int departmentID;
    private Day absentPeriod;

    public Teacher() {

    }

    public Teacher(int id, String firstName, String lastName, String password, String position, int departmentID,
            List<Lesson> lessons, Day absentPeriod) {
        super(id, firstName, lastName, password, lessons);
        this.departmentID = departmentID;
        this.position = position;
        this.absentPeriod = absentPeriod;
    }

    public static TeacherBuidler builder() {
        return new TeacherBuidler();
    }

    public static class TeacherBuidler {
        private int id;
        private String firstName;
        private String lastName;
        private String password;
        private List<Lesson> lessons;
        private String position;
        private int departmentID;
        private Day absentPeriod;

        public TeacherBuidler setID(int id) {
            this.id = id;
            return this;
        }

        public TeacherBuidler setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public TeacherBuidler setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public TeacherBuidler setPassword(String password) {
            this.password = password;
            return this;
        }

        public TeacherBuidler setLessons(List<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        public TeacherBuidler setPosition(String position) {
            this.position = position;
            return this;
        }

        public TeacherBuidler setDepartmentID(int departmentID) {
            this.departmentID = departmentID;
            return this;
        }

        public TeacherBuidler setAbsentPeriod(Day absentPeriod) {
            this.absentPeriod = absentPeriod;
            return this;
        }

        private TeacherBuidler construct(Object object) {
            return this;
        }

        public Teacher build() {
            return new Teacher(id, firstName, lastName, password, position, departmentID, lessons, absentPeriod);
        }

        public Teacher buildWith(Object object) {
            return construct(object).build();
        }
    }

    public String getPosition() {
        return position;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public Day getAbsentPeriod() {
        return absentPeriod;
    }

    public void setAbsentPeriod(Day absentPeriod) {
        this.absentPeriod = absentPeriod;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((absentPeriod == null) ? 0 : absentPeriod.hashCode());
        result = prime * result + departmentID;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        if (absentPeriod == null) {
            if (other.absentPeriod != null)
                return false;
        } else if (!absentPeriod.equals(other.absentPeriod))
            return false;
        if (departmentID != other.departmentID)
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }
}
