DROP TABLE IF EXISTS rooms CASCADE;
CREATE TABLE rooms
(
    room_id INT NOT NULL,
    capacity INT NOT NULL,
    CONSTRAINT rooms_pkey PRIMARY KEY (room_id)
 );
 
insert into rooms (room_id, capacity) values (101, 21);
insert into rooms (room_id, capacity) values (102, 26);
insert into rooms (room_id, capacity) values (201, 30);
insert into rooms (room_id, capacity) values (202, 15);
insert into rooms (room_id, capacity) values (203, 25);
insert into rooms (room_id, capacity) values (204, 30);
insert into rooms (room_id, capacity) values (301, 29);
insert into rooms (room_id, capacity) values (302, 26);
insert into rooms (room_id, capacity) values (303, 25);
insert into rooms (room_id, capacity) values (401, 19);
