DROP TABLE IF EXISTS users, files, users_files;

CREATE TABLE users (
id BIGINT AUTO_INCREMENT,
name VARCHAR(45) NOT NULL UNIQUE,
password CHAR(68) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE files (
id BIGINT AUTO_INCREMENT,
owner_id BIGINT NOT NULL,
name VARCHAR(45) NOT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_owner
	FOREIGN KEY (owner_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE users_files (
user_id BIGINT,
file_id BIGINT,
PRIMARY KEY (user_id, file_id),
CONSTRAINT fk_user
	FOREIGN KEY (user_id)
	REFERENCES users(id)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
CONSTRAINT fk_file
	FOREIGN KEY (file_id)
    REFERENCES files(id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

INSERT INTO users VALUES
	(100, 'user1', '{noop}password'),
	(200, 'user2', '{noop}password'),
    (300, 'user3', '{noop}password'),
    (400, 'user4', '{noop}password');

INSERT INTO files VALUES
	(10, 100, 'img001.jpg'),
    (20, 100, 'backup.zip'),
    (30, 200, 'photo001.jpg'),
    (40, 300, 'img001.jpg'),
    (50, 300, 'notes.txt');

INSERT INTO users_files VALUES
	(100, 10),
    (100, 20),
    (200, 30),
    (300, 40),
    (300, 50),
    (400, 10);