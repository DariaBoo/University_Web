package ua.foxminded.university.service;

import java.time.LocalDate;

public enum Holidays {
    NEW_YEAR(LocalDate.of(2022, 01, 01)), 
    EASTER_DAY(LocalDate.of(2022, 04, 17)),
    CHRISTMAS_DAY(LocalDate.of(2022, 12, 25));

    private LocalDate date;

    private Holidays(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return this.date;
    }

}
