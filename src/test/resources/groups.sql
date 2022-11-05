DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE groups
(
    group_id IDENTITY,
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
DROP TABLE IF EXISTS groups_lessons CASCADE;
CREATE TABLE groups_lessons
(    
    group_id INT REFERENCES groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE,
    lesson_id INT REFERENCES lessons (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (group_id, lesson_id)  
);
DROP TABLE IF EXISTS lessons_teachers CASCADE;
CREATE TABLE IF NOT EXISTS lessons_teachers
(    
    lesson_id INT REFERENCES lessons (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
    teacher_id INT REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (lesson_id, teacher_id)  
);