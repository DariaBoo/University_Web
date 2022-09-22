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

INSERT INTO staff VALUES(1, 'NAME', 'SURNAME', 'POSITION', 'USERNAME', '$2a$12$jKi06LU/pHmIpe3ROo9.Yu8RPZFBhM.nHIsfDS/zIfvFaamunZZhe', 'STAFF', true);
INSERT INTO staff VALUES(2, 'NAME', 'SURNAME', 'POSITION', 'ADMIN', '$2a$12$6z30j53hAMTU.B7SkOF.R.TPanfj/hdkmqRTuvNyqOV.EERATUssS', 'ADMIN', true);
INSERT INTO staff VALUES(3, 'NAME', 'SURNAME', 'POSITION', 'STAFF', '$2a$12$jKi06LU/pHmIpe3ROo9.Yu8RPZFBhM.nHIsfDS/zIfvFaamunZZhe', 'STAFF', true);