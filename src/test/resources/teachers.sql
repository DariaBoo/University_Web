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
DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles
(
     id SERIAL,
     name VARCHAR(10) UNIQUE NOT NULL,
     CONSTRAINT roles_pkey PRIMARY KEY (id)
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
INSERT INTO roles VALUES(1, 'TEACHER');