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

DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE groups
(
    group_id SERIAL,
    group_name VARCHAR(5) UNIQUE NOT NULL,
    department_id INT,
    isActive BOOLEAN,
    PRIMARY KEY(group_id)
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students
(
    id SERIAL,    
    user_id INT references users(id),
    group_id INT references groups(group_id),
    id_card VARCHAR(5) NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles
(
     id SERIAL,
     name VARCHAR(10) UNIQUE NOT NULL,
     CONSTRAINT roles_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users_roles CASCADE;
CREATE TABLE users_roles
(
     id SERIAL,
     user_id INT references users(id),
     role_id INT references roles(id),
     CONSTRAINT users_roles_pkey PRIMARY KEY (id)
);

INSERT INTO roles VALUES(1, 'STUDENT');
INSERT INTO groups VALUES(1, 'name', 1, true);
