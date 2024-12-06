
/*
---
these sql instructions layout the foundation for the postgresql database
---
*/

-- allow access to the database
CREATE DATABASE EXAMPLE_DB;
CREATE USER EXAMPLE_USER WITH ENCRYPTED PASSWORD 'Sup3rS3cret';
GRANT ALL PRIVILEGES ON DATABASE EXAMPLE_DB TO EXAMPLE_USER;
\c EXAMPLE_DB postgres
# You are now connected to database "EXAMPLE_DB" as user "postgres".
GRANT ALL ON SCHEMA public TO EXAMPLE_USER;

/* adding a default player for testing */
INSERT INTO table_name (col1, col2, ..., coln)
VALUES (val1, val2, ..., valn.);

INSERT INTO public.player_answers (username, password, answer_1, answer_2, answer_3, answer_4,
answer_5, answer_6, answer_7, currentPath) 
VALUES ('anakin', 'default', 'episode', '3', 'revenge', 'of', 'the', 'sith', 'jedi', '');

/*

database will contain user info like username and password for login, resuming progress 
there will be 7 riddles that one will have to solve and if they get all 7 answers then they will win
*/

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
    currentPath VARCHAR(255),
);