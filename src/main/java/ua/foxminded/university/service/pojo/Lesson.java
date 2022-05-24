package ua.foxminded.university.service.pojo;

import java.util.List;

public class Lesson {
    private int id;
    private String name;
    private String description;
    private List<Teacher> teachers;

    public Lesson() {
        
    }
    public Lesson(int id, String name, String description, List<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teachers = teachers;
    }
    
    public static LessonBuilder builder() {
        return new LessonBuilder();
    }
    
    public static class LessonBuilder {
        private int id;
        private String name;
        private String description;
        private List<Teacher> teachers;
        
        public LessonBuilder setID(int id) {
            this.id = id;
            return this;
        }

        public LessonBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public LessonBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public LessonBuilder setTeachers(List<Teacher> teachers) {
            this.teachers = teachers;
            return this;
        }
        
        public Lesson build() {
            return new Lesson(id, name, description, teachers);
        }        
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((teachers == null) ? 0 : teachers.hashCode());
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
        Lesson other = (Lesson) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (teachers == null) {
            if (other.teachers != null)
                return false;
        } else if (!teachers.equals(other.teachers))
            return false;
        return true;
    }       
}
