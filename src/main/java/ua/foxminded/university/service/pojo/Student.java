package ua.foxminded.university.service.pojo;

import java.util.List;

public class Student extends User {
    private int idCard;
    private int groupID;

    public Student(int id, String firstName, String lastName, int password, List<Course> courses, int idCard,
            int groupID) {
        super(id, firstName, lastName, password, courses);
        this.idCard = idCard;
        this.groupID = groupID;
    }
    public static StudentBuidler builder() {
        return new StudentBuidler();
    }
    public static class StudentBuidler {
        private int id;
        private String firstName;
        private String lastName;
        private int password;
        private List<Course> courses;
        private int idCard;
        private int groupID;
        
        public StudentBuidler setStudentID(int studentID) {
            return this;
        }
        public StudentBuidler setFirstName(String firstName) {
            return this;
        }
        public StudentBuidler setLastName(String lastName) {
            return this;
        }
        public StudentBuidler setPassword(int password) {
            return this;
        }
        public StudentBuidler setCourses(List<String> courses) {
            return this;
        }
        public StudentBuidler setIdCard(int idCard) {
            return this;
        }
        public StudentBuidler setGroupID(int groupID) {
            return this;
        }
        public Student buildWith(Object object) {
            return construct(object).build();
        }

        private StudentBuidler construct(Object object) {
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName, password, courses, idCard, groupID);
        }
    }
    
    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + groupID;
        result = prime * result + idCard;
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
        Student other = (Student) obj;
        if (groupID != other.groupID)
            return false;
        if (idCard != other.idCard)
            return false;
        return true;
    }

}
