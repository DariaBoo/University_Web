
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Alchemy','the study of transmutation of substances into other forms.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Herbology','the study of magical plants and how to take care of, utilise and combat them.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('History of Magic','the study of magical history.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Potions','the art of creating mixtures with magical effects.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Muggle Studies','the study of the Muggle culture "from a wizarding point of view".', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Dark Arts','the study of defensive techniques to defend against the Dark Arts.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Charms','the study of spells concerned with giving an object new properties.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Flying','the study of flying of broomsticks.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Magical Theory','the study of  magic from a purely theoretical view.', 'true');
INSERT INTO timetable.lessons (lesson_name, description, isActive) VALUES ('Ghoul Studies','the study of ghouls  and  other similar creatures.', 'true');


INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('CO-68', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('FW-72', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('OQ-07', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('SG-63', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('GW-21', 1, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('OB-14', 2, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('HN-86', 2, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('RI-38', 2, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('FD-15', 2, true);
INSERT INTO timetable.groups (group_name, department_id, isActive) VALUES ('CX-49', 2, true);

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

INSERT INTO timetable.roles VALUES (1, 'ADMIN');
INSERT INTO timetable.roles VALUES (2, 'STAFF');
INSERT INTO timetable.roles VALUES (3, 'TEACHER');
INSERT INTO timetable.roles VALUES (4, 'STUDENT');

INSERT INTO timetable.users_roles VALUES (1, 1, 4);
INSERT INTO timetable.users_roles VALUES (2, 2, 3);
INSERT INTO timetable.users_roles VALUES (3, 3, 1);
INSERT INTO timetable.users_roles VALUES (4, 4, 2);

INSERT INTO timetable.users VALUES (1, 'alumno', 'name', 'surname', '$2a$10$BwGHPnlWXpWbX6VVOakA6eUgNwbxk53I9HjcU6IvtZSogVre0Xli2', true);
INSERT INTO timetable.users VALUES (2, 'profesor', 'name1', 'surname1', '$2a$10$DauzvggXE48J1GMzuKId3uXTV4irpbZe6LPUXtT3Ddk0Hxh6oso1C', true);
INSERT INTO timetable.users VALUES (3, 'admin', 'name2', 'surname2', '$2a$10$URGH54SGiDLErty3BNqfB.kEtsOyiZ.Zr4OaGdKPRe0v0cObs9XSS', true);
INSERT INTO timetable.users VALUES (4, 'staff', 'name3', 'surname3', '$2a$10$0W3oL1jkgVr/0TYE3zQdx.FhCldM952mZq106SUcy6oF4rv7vcA5K', true);

INSERT INTO timetable.students VALUES (1, 1, 1, '2a-b');

INSERT INTO timetable.teachers VALUES (1, 2, 'position', 1);

INSERT INTO timetable.staff VALUES (1, 3, 'position');
INSERT INTO timetable.staff VALUES (2, 4, 'position');


insert into timetable.lessons_teachers (lesson_id, teacher_id) values (1, 1);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (1, 2);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (2, 3);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (2, 4);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (3, 5);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (3, 6);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (4, 7);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (4, 8);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (5, 9);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (5, 10);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (6, 1);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (6, 2);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (7, 3);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (7, 4);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (8, 5);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (8, 6);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (9, 7);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (9, 8);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (10, 9);
insert into timetable.lessons_teachers (lesson_id, teacher_id) values (10, 10);

insert into timetable.groups_lessons (group_id, lesson_id) values (1, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (1, 7);
insert into timetable.groups_lessons (group_id, lesson_id) values (1, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (1, 8);
insert into timetable.groups_lessons (group_id, lesson_id) values (1, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 6);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 9);
insert into timetable.groups_lessons (group_id, lesson_id) values (2, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 8);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 10);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (3, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 9);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 7);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 10);
insert into timetable.groups_lessons (group_id, lesson_id) values (4, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 10);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 8);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 9);
insert into timetable.groups_lessons (group_id, lesson_id) values (5, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 9);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 7);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 10);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (6, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (7, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (7, 10);
insert into timetable.groups_lessons (group_id, lesson_id) values (7, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (7, 9);
insert into timetable.groups_lessons (group_id, lesson_id) values (7, 6);
insert into timetable.groups_lessons (group_id, lesson_id) values (8, 6);
insert into timetable.groups_lessons (group_id, lesson_id) values (8, 4);
insert into timetable.groups_lessons (group_id, lesson_id) values (8, 7);
insert into timetable.groups_lessons (group_id, lesson_id) values (8, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (8, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (9, 6);
insert into timetable.groups_lessons (group_id, lesson_id) values (9, 7);
insert into timetable.groups_lessons (group_id, lesson_id) values (9, 8);
insert into timetable.groups_lessons (group_id, lesson_id) values (9, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (9, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (10, 5);
insert into timetable.groups_lessons (group_id, lesson_id) values (10, 1);
insert into timetable.groups_lessons (group_id, lesson_id) values (10, 2);
insert into timetable.groups_lessons (group_id, lesson_id) values (10, 3);
insert into timetable.groups_lessons (group_id, lesson_id) values (10, 10);

INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-01-01', 'NEW YEAR');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-01-06', 'THREE KINGS DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-04-17', 'EASTER DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-05-01', 'LABOUR DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-11-01', 'ALL SAINTS DAY');
INSERT INTO timetable.holidays (date, holiday) VALUES ('2022-12-25', 'CHRISTMAS DAY');

INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (1, '2022-01-10', '2022-01-12');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (1, '2022-03-01', '2022-03-06');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (1, '2022-04-08', '2022-04-11');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (1, '2022-06-17', '2022-06-18');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (1, '2023-01-01', '2023-01-01');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (2, '2022-01-10', '2022-01-12');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (2, '2023-01-01', '2023-01-01');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (3, '2022-01-10', '2022-01-12');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (3, '2023-01-01', '2023-01-01');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (4, '2022-01-10', '2022-01-12');
INSERT INTO timetable.teacherabsent (teacher_id, date_start, date_end) VALUES (4, '2023-01-01', '2023-01-01');
