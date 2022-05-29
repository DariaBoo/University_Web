package ua.foxminded.university.service.pojo;

public enum LessonTimePeriod {
    lesson1("08:00 - 09:20"),
    lesson2("09:30 - 10:50"),
    lesson3("11:00 - 12:20"),
    lesson4("12:30 - 13:50"),
    lesson5("14:00 - 15:20"),
    lesson6("15:30 - 16:50"),
    lesson7("17:00 - 18:20"),
    lesson8("18:30 - 19:50");
    
    private String timePeriod;
    
    private LessonTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
    
    public String getTimePeriod() {
        return timePeriod;
    }
}
