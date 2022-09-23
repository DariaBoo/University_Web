DROP TABLE IF EXISTS staff CASCADE;
CREATE TABLE staff
(
     id SERIAL,
     first_name VARCHAR(30)  NOT NULL,
     last_name VARCHAR(30)  NOT NULL,
     position VARCHAR(30) NOT NULL,
     user_name VARCHAR(30)  NOT NULL,
     password VARCHAR(255) NOT NULL,
     role VARCHAR(10) NOT NULL,
     isActive BOOLEAN,
     CONSTRAINT staff_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students
(
    id SERIAL NOT NULL,    
    first_name VARCHAR(30)  NOT NULL,
    last_name VARCHAR(30)  NOT NULL,
    group_id INT,
    password VARCHAR(255) NOT NULL,
    id_card VARCHAR(5) UNIQUE NOT NULL,
    isActive BOOLEAN,
    role VARCHAR(10) NOT NULL,
    user_name VARCHAR(30) UNIQUE NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (id),  
    CONSTRAINT report_unique_constraint
    UNIQUE (first_name, last_name, id_card)
);

DROP TABLE IF EXISTS teachers CASCADE;
CREATE TABLE teachers
(
     id SERIAL,
     first_name VARCHAR(30)  NOT NULL,
     last_name VARCHAR(30)  NOT NULL,
     position VARCHAR(30) NOT NULL,
     password VARCHAR(255) NOT NULL,
     department_id INT,
     isActive BOOLEAN,
     role VARCHAR(10) NOT NULL,
     user_name VARCHAR(30) UNIQUE NOT NULL,
     CONSTRAINT teachers_pkey PRIMARY KEY (id),
     UNIQUE (first_name, last_name)
);

INSERT INTO staff VALUES(1, 'NAME', 'SURNAME', 'POSITION', 'USERNAME', '$2a$12$jKi06LU/pHmIpe3ROo9.Yu8RPZFBhM.nHIsfDS/zIfvFaamunZZhe', 'STAFF', true);
INSERT INTO staff VALUES(2, 'NAME', 'SURNAME', 'POSITION', 'ADMIN', '$2a$12$6z30j53hAMTU.B7SkOF.R.TPanfj/hdkmqRTuvNyqOV.EERATUssS', 'ADMIN', true);
INSERT INTO staff VALUES(3, 'NAME', 'SURNAME', 'POSITION', 'STAFF', '$2a$12$jKi06LU/pHmIpe3ROo9.Yu8RPZFBhM.nHIsfDS/zIfvFaamunZZhe', 'STAFF', true);

INSERT INTO students VALUES(1, 'NAME', 'SURNAME', 1, '$2a$10$cS44nbWAC4VRI47tJchy3emzaf.HrGmjEOFpi2zXFCNeo84eXgSqu', 'ID1', true, 'USER', 'student_1');

INSERT INTO teachers VALUES(1, 'NAME', 'SURNAME', 'POSITION', '$2a$12$kNcBjZYsE3E8.H9TLpElvuGufHoOQDfacWkiVF7kl.m2DpZOOaboa', 1, true, 'USER', 'teacher_1');







