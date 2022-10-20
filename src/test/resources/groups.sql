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
