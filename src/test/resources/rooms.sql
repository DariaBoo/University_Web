DROP TABLE IF EXISTS rooms CASCADE;
CREATE TABLE rooms
(
    room_id INT NOT NULL,
    capacity INT NOT NULL,
    CONSTRAINT rooms_pkey PRIMARY KEY (room_id)
 );
 