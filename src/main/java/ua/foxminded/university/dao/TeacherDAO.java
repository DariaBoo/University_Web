package ua.foxminded.university.dao;

import ua.foxminded.university.service.pojo.Teacher;

public interface TeacherDAO {
    public void addTeacher(Teacher teacher);
    public void removeTeacher(int teacherID);
    public void addLesson(int lessonID);
    public void removeLesson(int lessonID);
}
