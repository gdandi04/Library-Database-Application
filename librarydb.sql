DROP DATABASE IF EXISTS univeristyLibrarydb;
CREATE DATABASE universityLibrarydb;

USE universityLibrarydb;

CREATE TABLE library
(
library_id					INT				PRIMARY KEY		AUTO_INCREMENT,
location					VARCHAR(30)		NOT NULL,
library_name				VARCHAR(30)		NOT NULL,
affiliated_university		VARCHAR(30)		NOT NULL
);

CREATE TABLE library_staff
(
staff_id		INT				PRIMARY KEY		AUTO_INCREMENT,
staff_name		VARCHAR(30)		NOT NULL,
address			VARCHAR(100),
salary			DECIMAL			NOT NULL,
library_id		INT				NOT NULL,
CONSTRAINT staff_lib_id
FOREIGN KEY	(library_id)
REFERENCES library(library_id)
ON UPDATE RESTRICT ON DELETE RESTRICT
);


CREATE TABLE university_member
(
member_id		INT				PRIMARY KEY		AUTO_INCREMENT,
member_name		VARCHAR(30)		NOT NULL,
address			VARCHAR(100)	NOT NULL,
library_id		INT				NOT NULL,
staff_id		INT				NOT NULL,
CONSTRAINT member_lib_id
FOREIGN KEY	(library_id)
REFERENCES library(library_id)
ON UPDATE RESTRICT ON DELETE RESTRICT,
CONSTRAINT staff_registers_mem
FOREIGN KEY (staff_id) REFERENCES library_staff(staff_id)
ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE media 
(
media_id INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE cd
(
cd_id			INT				PRIMARY KEY		AUTO_INCREMENT,
album_name		VARCHAR(100)	NOT NULL,
artist			VARCHAR(30)		NOT NULL,
producer		VARCHAR(30)		NOT NULL,
num_copies		INT				NOT NULL,
year_released	DATE,
song_list		TEXT,
CONSTRAINT cd_fk_media
FOREIGN KEY (cd_id)
REFERENCES media(media_id)
ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ebook
(
ebook_id INT PRIMARY KEY AUTO_INCREMENT,
title VARCHAR(100)NOT NULL,
author VARCHAR(100)NOT NULL,
pageCount INT NOT NULL,
category VARCHAR(100)NOT NULL,
availInPrint BOOLEAN NOT NULL,
plot VARCHAR(300) NOT NULL,
printPubYear INT NOT NULL,
ePubYear INT NOT NULL,
CONSTRAINT fk_ebook_id_media_id FOREIGN KEY (ebook_id) REFERENCES media (media_id)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE video 
(
video_id		INT				PRIMARY KEY		AUTO_INCREMENT,
title			VARCHAR(100)	NOT NULL,
director		VARCHAR(30)		NOT NULL,
actors			TEXT			NOT NULL,
genre			VARCHAR(30)		NOT NULL,
plot			TEXT			NOT NULL,
runtime			INT				NOT NULL,
num_copies		INT				NOT NULL,
year_released	DATE			NOT NULL,
CONSTRAINT vid_fk_media
FOREIGN KEY (video_id)
REFERENCES media(media_id)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT vid_genre
CHECK (genre = 'Comedy'
OR genre = 'Drama' 
OR genre = 'Horror'
OR genre = 'Children'
OR genre = 'Romance'
OR genre = 'Action')
);

CREATE TABLE book
(
book_id INT PRIMARY KEY AUTO_INCREMENT,
title VARCHAR(100) NOT NULL,
author VARCHAR(100) NOT NULL,
pageCount INT NOT NULL,
pubYear INT NOT NULL,
category VARCHAR(100) NOT NULL,
availAsEbook BOOLEAN NOT NULL,
plot VARCHAR(300) NOT NULL,
numCopies INT NOT NULL,
CONSTRAINT fk_book_id_media_id FOREIGN KEY (book_id) REFERENCES media (media_id)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE member_rents_media 
(
member_id INT NOT NULL,
media_id INT NOT NULL,
PRIMARY KEY(member_id, media_id),
CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES university_member (member_id)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_media_id FOREIGN KEY (media_id) REFERENCES media (media_id)
ON UPDATE RESTRICT ON DELETE RESTRICT
);