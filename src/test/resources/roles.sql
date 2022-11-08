DROP TABLE IF EXISTS holidays;
CREATE TABLE holidays
(
    id SERIAL,
    holiday VARCHAR(20) NOT NULL,
    date DATE NOT NULL,    
    CONSTRAINT holiday_pkey PRIMARY KEY (id)
);