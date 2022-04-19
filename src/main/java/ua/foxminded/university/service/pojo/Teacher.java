package ua.foxminded.university.service.pojo;

import java.util.List;

public class Teacher extends User {
    private String position;

    public Teacher(int id, String firstName, String lastName, int password, String position, List<Course> courses) {
        super(id, firstName, lastName, password, courses);
        this.position = position;
    }
    public static TeacherBuidler builder() {
        return new TeacherBuidler();
    }
    public static class TeacherBuidler {
        private int id;
        private String firstName;
        private String lastName;
        private int password;
        private List<Course> courses;
        private String position;
        
        public TeacherBuidler setTeacherID(int teacherID) {
            return this;
        }
        public TeacherBuidler setFirstName(String firstName) {
            return this;
        }
        public TeacherBuidler setLastName(String lastName) {
            return this;
        }
        public TeacherBuidler setPassword(int password) {
            return this;
        }
        public TeacherBuidler setCourses(List<String> courses) {
            return this;
        }       
        public TeacherBuidler setPosition(String position) {
            return this;
        }
        public Teacher buildWith(Object object) {
            return construct(object).build();
        }

        private TeacherBuidler construct(Object object) {
            return this;
        }

        public Teacher build() {
            return new Teacher(id, firstName, lastName, password, position, courses);
        }
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
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
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }
}
