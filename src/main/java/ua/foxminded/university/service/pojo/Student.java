package ua.foxminded.university.service.pojo;

import java.util.List;

public class Student extends User {

    private String idCard;
    private int groupID;

    public Student() {

    }

    public Student(int id, String firstName, String lastName, String password, List<Lesson> lessons, String idCard,
            int groupID) {
        super(id, firstName, lastName, password, lessons);
        this.idCard = idCard;
        this.groupID = groupID;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private int id;
        private String firstName;
        private String lastName;
        private String password;
        private List<Lesson> lessons;
        private String idCard;
        private int groupID;

        public StudentBuilder setID(int studentID) {
            id = studentID;
            return this;
        }

        public StudentBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public StudentBuilder setLessons(List<Lesson> lessons) {
            this.lessons = lessons;
            return this;
        }

        public StudentBuilder setIdCard(String idCard) {
            this.idCard = idCard;
            return this;
        }

        public StudentBuilder setGroupID(int groupID) {
            this.groupID = groupID;
            return this;
        }

        public Student buildWith(Object object) {
            return construct(object).build();
        }

        private StudentBuilder construct(Object object) {
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName, password, lessons, idCard, groupID);
        }
    }

    public String getIdCard() {
        return idCard;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + groupID;
        result = prime * result + ((idCard == null) ? 0 : idCard.hashCode());
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
        if (idCard == null) {
            if (other.idCard != null)
                return false;
        } else if (!idCard.equals(other.idCard))
            return false;
        return true;
    }
}
