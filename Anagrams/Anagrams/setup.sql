DROP DATABASE IF EXISTS words;
CREATE DATABASE words;
CONNECT words;

CREATE TABLE words(
	word VARCHAR(7),
	points INTEGER
);