DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE groups
(
    group_id SERIAL,
    group_name VARCHAR(5) UNIQUE NOT NULL,
    department_id INT,
    isActive BOOLEAN,
    PRIMARY KEY(group_id)
);
DROP TABLE IF EXISTS lessons CASCADE;
CREATE TABLE lessons
(
    lesson_id SERIAL,
    lesson_name VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(100),
    isActive BOOLEAN,
    PRIMARY KEY (lesson_id)
);
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    id SERIAL,  
    user_name VARCHAR(30) UNIQUE NOT NULL,
    first_name VARCHAR(30)  NOT NULL,
    last_name VARCHAR(30)  NOT NULL,    
    password VARCHAR(255) NOT NULL,
    isActive BOOLEAN,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);
DROP TABLE IF EXISTS teachers CASCADE;
CREATE TABLE teachers
(
     id SERIAL,
     user_id INT references users(id),
     position VARCHAR(30) NOT NULL,
     department_id INT,
     CONSTRAINT teachers_pkey PRIMARY KEY (id)
);
DROP TABLE IF EXISTS rooms CASCADE;
CREATE TABLE rooms
(
    room_id INT NOT NULL,
    capacity INT NOT NULL,
    CONSTRAINT rooms_pkey PRIMARY KEY (room_id)
 );
DROP TABLE IF EXISTS timetable;
CREATE TABLE timetable
(
    id SERIAL,
    date DATE NOT NULL,
    time_period VARCHAR(13) NOT NULL,
    lesson_id INT REFERENCES lessons (lesson_id),
    group_id INT REFERENCES groups (group_id),
    teacher_id INT REFERENCES teachers (id),
    room_id INT REFERENCES rooms (room_id),
    CONSTRAINT timetable_pkey PRIMARY KEY (id)
);
INSERT INTO groups VALUES (1, 'group', 1, 'true');
INSERT INTO lessons VALUES (1, 'lesson', 'description','true');
INSERT INTO users VALUES (1, 'username', 'name', 'surname', 'password', 'true');
INSERT INTO teachers VALUES (1, 1, 'position', 1);
INSERT INTO rooms VALUES (101, 20);
INSERT INTO timetable VALUES (1, '2023-04-01', '08:00 - 09:20', 1, 1, 1, 101);
INSERT INTO timetable VALUES (2, '2023-04-02', '08:00 - 09:20', 1, 1, 1, 101);
INSERT INTO timetable VALUES (3, '2023-04-03', '08:00 - 09:20', 1, 1, 1, 101);

