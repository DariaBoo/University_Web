package ua.foxminded.university.service.pojo;

import java.util.List;

public class Group {

    private int id;
    private String name;
    private int departmentID;
    private List<Student> students;

    public Group() {

    }

    public Group(int id, String name, int departmentID, List<Student> students) {
        this.id = id;
        this.name = name;
        this.departmentID = departmentID;
        this.students = students;
    }

    public static GroupBuilder builder() {
        return new GroupBuilder();
    }

    public static class GroupBuilder {
        private int id;
        private String name;
        private int departmentID;
        private List<Student> students;

        public GroupBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public GroupBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GroupBuilder setDepartmentID(int departmentID) {
            this.departmentID = departmentID;
            return this;
        }

        public GroupBuilder setStudents(List<Student> students) {
            this.students = students;
            return this;
        }

        public Group build() {
            return new Group(id, name, departmentID, students);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + departmentID;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((students == null) ? 0 : students.hashCode());
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
        Group other = (Group) obj;
        if (departmentID != other.departmentID)
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (students == null) {
            if (other.students != null)
                return false;
        } else if (!students.equals(other.students))
            return false;
        return true;
    }
}
