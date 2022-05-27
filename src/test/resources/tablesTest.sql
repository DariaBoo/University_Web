CREATE SCHEMA IF NOT EXISTS timetable AUTHORIZATION sa;

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
    student_id SERIAL,    
    first_name VARCHAR(30)  NOT NULL,
    last_name VARCHAR(30)  NOT NULL,
    group_id INT references timetable.groups(group_id),
    password VARCHAR(10) NOT NULL,
    id_card VARCHAR(5) NOT NULL,
    isActive BOOLEAN,
    CONSTRAINT students_pkey PRIMARY KEY (student_id)
);

DROP TABLE IF EXISTS timetable.teachers CASCADE;
CREATE TABLE timetable.teachers
(
     teacher_id SERIAL,
     first_name VARCHAR(30)  NOT NULL,
     last_name VARCHAR(30)  NOT NULL,
     position VARCHAR(30) NOT NULL,
     password VARCHAR(10) NOT NULL,
     department_id INT,
     isActive BOOLEAN,
     CONSTRAINT teachers_pkey PRIMARY KEY (teacher_id)
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
    teacher_id INT REFERENCES timetable.teachers (teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (lesson_id, teacher_id)  
);

DROP TABLE IF EXISTS timetable.teacherAbsent CASCADE;
CREATE TABLE timetable.teacherAbsent
(
    id SERIAL,
    teacher_id INT REFERENCES timetable.teachers (teacher_id) ON UPDATE CASCADE,
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
    teacher_id INT REFERENCES timetable.teachers (teacher_id),
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

INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Alchemy','the study of transmutation of substances into other forms.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Herbology','the study of magical plants and how to take care of, utilise and combat them.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('History of Magic','the study of magical history.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Potions','the art of creating mixtures with magical effects.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Muggle Studies','the study of the Muggle culture "from a wizarding point of view".', 'true');

INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('CO-68', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('FW-72', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('OQ-07', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('SG-63', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('GW-21', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('OB-14', 2, true);


insert into timetable.rooms (room_id, capacity) values (101, 21);
insert into timetable.rooms (room_id, capacity) values (102, 26);
insert into timetable.rooms (room_id, capacity) values (201, 30);
insert into timetable.rooms (room_id, capacity) values (202, 15);
insert into timetable.rooms (room_id, capacity) values (203, 25);
insert into timetable.rooms (room_id, capacity) values (204, 30);
insert into timetable.rooms (room_id, capacity) values (301, 29);
insert into timetable.rooms (room_id, capacity) values (302, 26);
insert into timetable.rooms (room_id, capacity) values (303, 25);
insert into timetable.rooms (room_id, capacity) values (401, 19);

INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Harry', 'Potter', 1, 1234, '1IE', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Mandy', 'Finch-Fletchley', 1, 1234, '2WC', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Pansy', 'Brocklehurst', 1, 1234, '3SN', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Michael', 'Granger', 1, 1234, '4LV', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Parkinson', 1, 1234, '5QL', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hermione', 'Thomas', 1, 1234, '6JH', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Bones', 1, 1234, '7YC', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Finnigan', 1, 1234, '8KU', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Weasley', 1, 1234, '9PH', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Thomas', 1, 1234, '10WP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Padma', 'Potter', 1, 1234, '11ZG', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Corner', 1, 1234, '12PB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Boot', 1, 1234, '13PC', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Finch-Fletchley', 1, 1234, '14OL', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Turpin', 1, 1234, '15IU', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Patil', 1, 1234, '16ID', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Longbottom', 1, 1234, '17DA', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Padma', 'Finch-Fletchley', 1, 1234, '18SA', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Abbott', 1, 1234, '19LN', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Terry', 'Thomas', 1, 1234, '20JW', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Longbottom', 1, 1234, '21JP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Susan', 'Potter', 1, 1234, '22HK', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Turpin', 1, 1234, '23KY', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Thomas', 1, 1234, '24HU', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ronald', 'Brown', 1, 1234, '25BJ', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hermione', 'Longbottom', 1, 1234, '26AN', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Potter', 1, 1234, '27HB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Potter', 1, 1234, '28JK', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Dean', 'Granger', 1, 1234, '29VV', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Parvati', 'Brocklehurst', 2, 1234, '2NJ', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Brocklehurst', 2, 1234, '4CC', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Dean', 'Nott', 2, 1234, '6PO', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Patil', 2, 1234, '8SL', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Weasley', 2, 1234, '10JW', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Macmillan', 2, 1234, '12HX', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Patil', 2, 1234, '14EB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Finnigan', 2, 1234, '16QD', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Finnigan', 2, 1234, '18IR', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hermione', 'Abbott', 2, 1234, '20PD', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Michael', 'Corner', 2, 1234, '22YK', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hermione', 'Goldstein', 2, 1234, '24NP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Boot', 2, 1234, '26BO', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Anthony', 'Patil', 2, 1234, '28BF', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Goldstein', 2, 1234, '30WC', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Finch-Fletchley', 2, 1234, '32EY', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Michael', 'Patil', 2, 1234, '34SU', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hermione', 'Weasley', 3, 1234, '3LN', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Nott', 3, 1234, '6HO', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Parvati', 'Patil', 3, 1234, '9AT', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Brown', 3, 1234, '12LA', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Boot', 3, 1234, '15VK', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Mandy', 'Macmillan', 3, 1234, '18AV', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Turpin', 3, 1234, '21AY', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Terry', 'Turpin', 3, 1234, '24IH', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Terry', 'Boot', 3, 1234, '27ZX', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Dean', 'Goldstein', 3, 1234, '30LD', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Patil', 3, 1234, '33BR', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hannah', 'Finnigan', 4, 1234, '4BP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hermione', 'Finnigan', 4, 1234, '8SC', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lisa', 'Goldstein', 4, 1234, '12GS', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Parvati', 'Weasley', 4, 1234, '16BM', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Brown', 4, 1234, '20QM', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Dean', 'Finch-Fletchley', 4, 1234, '24NF', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lisa', 'Nott', 4, 1234, '28QE', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Longbottom', 4, 1234, '32DP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ronald', 'Abbott', 4, 1234, '36YI', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Terry', 'Brocklehurst', 4, 1234, '40KW', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Thomas', 4, 1234, '44EP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lisa', 'Parkinson', 4, 1234, '48AF', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Terry', 'Patil', 4, 1234, '52JB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Harry', 'Patil', 4, 1234, '56WL', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ronald', 'Boot', 4, 1234, '60FY', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Padma', 'Boot', 4, 1234, '64SG', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Pansy', 'Finnigan', 4, 1234, '68VS', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Dean', 'Nott', 4, 1234, '72ND', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Mandy', 'Potter', 4, 1234, '76TD', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Parvati', 'Granger', 4, 1234, '80QM', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hannah', 'Parkinson', 4, 1234, '84BB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lisa', 'Patil', 4, 1234, '88ED', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Mandy', 'Brown', 4, 1234, '92PI', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Potter', 4, 1234, '96CJ', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Theodore', 'Goldstein', 4, 1234, '100PJ', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Seamus', 'Brocklehurst', 4, 1234, '104UU', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Anthony', 'Corner', 5, 1234, '5NL', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Terry', 'Nott', 5, 1234, '10CS', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Neville', 'Goldstein', 5, 1234, '15CE', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Padma', 'Potter', 5, 1234, '20FV', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Parvati', 'Macmillan', 5, 1234, '25BB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Pansy', 'Patil', 5, 1234, '30EE', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Parvati', 'Potter', 5, 1234, '35YS', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Nott', 5, 1234, '40TS', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Padma', 'Granger', 5, 1234, '45YF', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Pansy', 'Abbott', 5, 1234, '50XJ', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Susan', 'Potter', 5, 1234, '55EP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Neville', 'Brocklehurst', 5, 1234, '60IM', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lisa', 'Longbottom', 5, 1234, '65DU', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Anthony', 'Potter', 5, 1234, '70FL', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Dean', 'Longbottom', 5, 1234, '75SS', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Lavender', 'Patil', 5, 1234, '80LA', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Pansy', 'Boot', 5, 1234, '85ZP', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hannah', 'Bones', 5, 1234, '90RM', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Padma', 'Parkinson', 5, 1234, '95TK', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Justin', 'Boot', 5, 1234, '100KW', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Hannah', 'Patil', 5, 1234, '105PF', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ronald', 'Parkinson', 5, 1234, '110WB', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Ernie', 'Macmillan', 5, 1234, '115MJ', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Susan', 'Boot', 5, 1234, '120KO', 'true');
INSERT INTO timetable.students (first_name, last_name, group_id, password, id_card, isActive) VALUES ('Anthony', 'Finnigan', 5, 1234, '125RF', 'true');


INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Albus', 'Dumbledore', 'professor', 555, 1, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Minerva', 'McGonagall', 'lecturer', 555, 1, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Severus', 'Snape', 'professor', 555, 1, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Filius', 'Flitwick', 'professor', 555, 1, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Remus', 'Lupin', 'assistant', 555, 1, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Barty', 'Crouch', 'professor', 555, 2, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Horace', 'Slughorn', 'lecturer', 555, 2, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Dolores', 'Umbridge', 'assistant', 555, 2, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Rubeus', 'Hagrid', 'lecturer', 555, 2, 'true');
INSERT INTO timetable.teachers (first_name, last_name, position, password, department_id, isActive) VALUES ('Pomona', 'Sprout', 'professor', 555, 2, 'true');

insert into timetable.lessons_teachers (lesson_id, teacher_id) values (1, 1);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (1, 2);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (2, 3);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (2, 4);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (3, 5);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (3, 1);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (4, 2);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (4, 3);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (5, 4);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (5, 5);

insert into timetable.groups_lessons (group_id, lesson_id) values (1, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (1, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (1, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 5); 

INSERT INTO timetable.timetable (date, time_period, lesson_id, group_id) VALUES ('2022-04-20', '08:00 - 09:20', 4, 2);
INSERT INTO timetable.timetable (date, time_period, lesson_id, group_id, teacher_id, room_id) VALUES ('2023-04-01', '08:00 - 09:20', 1, 1, 1, 201);
INSERT INTO timetable.timetable (date, time_period, lesson_id, group_id, teacher_id, room_id) VALUES ('2023-04-01', '08:00 - 09:20', 1, 1, 2, 201);
INSERT INTO timetable.timetable (date, time_period, lesson_id, group_id, teacher_id, room_id) VALUES ('2023-04-02', '08:00 - 09:20', 1, 1, 1, 201);

INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-01-01', 'NEW YEAR');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-01-06', 'THREE KINGS DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-04-17', 'EASTER DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-05-01', 'LABOUR DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-11-01', 'ALL SAINTS DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-12-25', 'CHRISTMAS DAY');


UPDATE timetable.students SET first_name = 'Marry', last_name = 'Potter' WHERE student_id = 1000 AND EXISTS (SELECT student_id FROM timetable.students);

