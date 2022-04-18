package ua.foxminded.university.dao;

import ua.foxminded.university.service.pojo.Student;

public interface StudentDAO {
    public void addStudent(Student student);

    public void removeStudent(int studentID);

    public void setGroup(int groupID);

    public void removeGroup(int groupID);

    public void setLesson(int lessonID);

    public void removeLesson(int lessonID);

}
