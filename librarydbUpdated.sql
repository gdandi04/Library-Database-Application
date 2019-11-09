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
ON UPDATE ON DELETE 
);


CREATE TABLE university_member
(
member_id		INT		

);

CREATE TABLE media 
(
media_id		INT		PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE cd
(

);

CREATE TABLE ebook
(

);

CREATE TABLE video 
(

);

CREATE TABLE book
(

);





