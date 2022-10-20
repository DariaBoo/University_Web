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

INSERT INTO roles VALUES (1, 'ADMIN');
INSERT INTO users VALUES (1, 'admin', 'name', 'surname', '$2a$12$RDtcv0Yz8uGCl2vZHlbkV.3vHyxP2XK9Rx.kFPM2kF5/i3QYPLZEO', true);
INSERT INTO users_roles VALUES (1, 1, 1);