
/*
---
setting up postgresql database

*/

/*
connecting to database, in linux
*/
sudo -u postgres psql


-- in postgres, you can change around username and password as you wish, and database name of course
CREATE DATABASE labyrinth;
CREATE USER ryan WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE labyrinth TO ryan;
\c labyrinth postgres
# You are now connected to database "labyrinth" as user "postgres".
GRANT ALL ON SCHEMA public TO ryan;


-- now you want create a table in the database
-- switching to the user you made also
\c labyrinth ryan 
-- or whatever username and db name you have
-- this may not work, it may say Peer authentication failed
-- in this case do sudo find /etc/ -name pg_hba.conf
-- then sudo nano /etc/postgresql/<version>/main/pg_hba.conf

/*

find the line that looks like

local   all             all                                     peer

-> change peer to md5, save and quit

peer means that postgresql authenticates by checking the postgresql username to the opertating system username
if they don't match theres no auth
md5 authenticates via password
this is still secure since passwords are encrypted

now run 
sudo systemctl restart postgresql

*/


-- now you create the table for the application
CREATE TABLE public.player_answers (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answer_1 VARCHAR(255),
    answer_2 VARCHAR(255),
    answer_3 VARCHAR(255),
    answer_4 VARCHAR(255),
    answer_5 VARCHAR(255),
    answer_6 VARCHAR(255),
    answer_7 VARCHAR(255),
    currentpath VARCHAR(255)
);


-- now if you run SELECT * FROM public.player_answers; 
-- you should see your table, when a new user is created in the login/create user page
-- a new instance will be initialized in the table