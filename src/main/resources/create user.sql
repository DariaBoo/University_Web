CREATE USER admin WITH PASSWORD '1234' CREATEDB;

GRANT ALL ON DATABASE university TO admin;   
GRANT ALL PRIVILEGES ON DATABASE university TO admin;
