CREATE SCHEMA IF NOT EXISTS timetable AUTHORIZATION admin;

DROP TABLE IF EXISTS timetable.groups CASCADE;
CREATE TABLE timetable.groups
(
    group_id SERIAL,
    group_name VARCHAR(5) UNIQUE NOT NULL,
    department_id INT,
    isActive BOOLEAN,
    PRIMARY KEY(group_id)
);

DROP TABLE IF EXISTS timetable.lessons CASCADE;
CREATE TABLE timetable.lessons
(
    lesson_id SERIAL,
    lesson_name VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(100),
    isActive BOOLEAN,
    PRIMARY KEY (lesson_id)
);

DROP TABLE IF EXISTS timetable.students CASCADE;
CREATE TABLE timetable.students
(
    id SERIAL,    
    first_name VARCHAR(30)  NOT NULL,
    last_name VARCHAR(30)  NOT NULL,
    group_id INT references timetable.groups(group_id),
    password VARCHAR(10) NOT NULL,
    id_card VARCHAR(5) NOT NULL,
    isActive BOOLEAN,
    CONSTRAINT students_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS timetable.teachers CASCADE;
CREATE TABLE timetable.teachers
(
     id SERIAL,
     first_name VARCHAR(30)  NOT NULL,
     last_name VARCHAR(30)  NOT NULL,
     position VARCHAR(30) NOT NULL,
     password VARCHAR(10) NOT NULL,
     department_id INT,
     isActive BOOLEAN,
     CONSTRAINT teachers_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS timetable.rooms CASCADE;
CREATE TABLE timetable.rooms
(
    room_id INT NOT NULL,
    capacity INT NOT NULL,
    CONSTRAINT rooms_pkey PRIMARY KEY (room_id)
 );

DROP TABLE IF EXISTS timetable.groups_lessons CASCADE;
CREATE TABLE timetable.groups_lessons
(    
    group_id INT REFERENCES timetable.groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE,
    lesson_id INT REFERENCES timetable.lessons (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (group_id, lesson_id)  
);

DROP TABLE IF EXISTS timetable.lessons_teachers CASCADE;
CREATE TABLE IF NOT EXISTS timetable.lessons_teachers
(    
    lesson_id INT REFERENCES timetable.lessons (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
    teacher_id INT REFERENCES timetable.teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (lesson_id, teacher_id)  
);

DROP TABLE IF EXISTS timetable.teacherAbsent CASCADE;
CREATE TABLE timetable.teacherAbsent
(
    id SERIAL,
    teacher_id INT REFERENCES timetable.teachers (id) ON UPDATE CASCADE,
    date_start DATE NOT NULL,
    date_end DATE NOT NULL,
    reason VARCHAR(30) ,
    CONSTRAINT teacherAbsent_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS timetable.timetable;
CREATE TABLE timetable.timetable
(
    id SERIAL,
    date DATE NOT NULL,
    time_period VARCHAR(13) NOT NULL,
    lesson_id INT REFERENCES timetable.lessons (lesson_id),
    group_id INT REFERENCES timetable.groups (group_id),
    teacher_id INT REFERENCES timetable.teachers (id),
    room_id INT REFERENCES timetable.rooms (room_id),
    CONSTRAINT timetable_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS timetable.holidays;
CREATE TABLE timetable.holidays
(
    id SERIAL,
    holiday VARCHAR(20) NOT NULL,
    date DATE NOT NULL,    
    CONSTRAINT holiday_pkey PRIMARY KEY (id)
);
