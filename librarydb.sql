DROP DATABASE IF EXISTS universityLibrarydb;
CREATE DATABASE universityLibrarydb;

USE universityLibrarydb;

CREATE TABLE if not exists library
(
library_id					INT				PRIMARY KEY		AUTO_INCREMENT,
location					VARCHAR(255)		NOT NULL,
 library_name				VARCHAR(255)		NOT NULL,
 affiliated_university		VARCHAR(255)		NOT NULL
);

CREATE TABLE library_staff
(
staff_id		INT				PRIMARY KEY		AUTO_INCREMENT,
staff_name		VARCHAR(30)		NOT NULL,
street			VARCHAR(100)	NOT NULL,
 city			VARCHAR(100)	NOT NULL,
 state			VARCHAR(3)		NOT NULL,
 zipcode			INT				NOT NULL,
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
member_last_name		VARCHAR(30)		NOT NULL,
 member_first_name		VARCHAR(30)		NOT NULL,
 street			VARCHAR(100)	NOT NULL,
 city			VARCHAR(100)	NOT NULL,
 state			VARCHAR(3)		NOT NULL,
 zipcode			INT				NOT NULL,
library_id		INT				NOT NULL,
CONSTRAINT member_lib_id
FOREIGN KEY	(library_id)
REFERENCES library(library_id)
ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE media 
(
media_id INT AUTO_INCREMENT,
 media_title VARCHAR(255) NOT NULL,
 PRIMARY KEY (media_id, media_title)
);

CREATE TABLE cd
(
cd_id			INT,
album_name		VARCHAR(225)	NOT NULL,
artist			VARCHAR(30)		NOT NULL,
producer		VARCHAR(30)		NOT NULL,
genre VARCHAR(30)		NOT NULL,
num_copies		INT				NOT NULL,
year_released	DATE,
song_list		TEXT,
PRIMARY KEY (cd_id, album_name),
CONSTRAINT cd_fk_media
FOREIGN KEY (cd_id, album_name)
 REFERENCES media(media_id, media_title)
ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ebook
(
ebook_id INT ,
title VARCHAR(255)NOT NULL,
author VARCHAR(100)NOT NULL,
pageCount INT NOT NULL,
genre VARCHAR(100)NOT NULL,
num_copies INT NOT NULL,
availInPrint BOOLEAN NOT NULL,
plot VARCHAR(300) NOT NULL,
printPubYear INT NOT NULL,
ePubYear INT NOT NULL,
PRIMARY KEY (ebook_id, title),
 CONSTRAINT fk_ebook_id_media_id FOREIGN KEY (ebook_id, title) REFERENCES media (media_id, media_title)
 	ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE video 
(
video_id		INT				PRIMARY KEY,
title			VARCHAR(100)	NOT NULL,
director		VARCHAR(30)		NOT NULL,
actors			TEXT			NOT NULL,
genre			VARCHAR(30)		NOT NULL,
num_copies		INT				NOT NULL,
plot			TEXT			NOT NULL,
runtime			INT				NOT NULL,
year_released	INT			NOT NULL,
CONSTRAINT vid_fk_media
FOREIGN KEY (video_id, title)
REFERENCES media(media_id, media_title)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT vid_genre
CHECK (genre = 'Comedy'
OR genre = 'Drama' 
OR genre = 'Horror'
OR genre = 'Children'
OR genre = 'Romance'
OR genre = 'Action'
OR genre = 'Fantasy')
);

CREATE TABLE book
(
book_id INT PRIMARY KEY,
title VARCHAR(100) NOT NULL,
author VARCHAR(100) NOT NULL,
pageCount INT NOT NULL,
genre VARCHAR(100) NOT NULL,
num_copies INT NOT NULL,
pubYear INT NOT NULL,
availAsEbook BOOLEAN NOT NULL,
plot VARCHAR(300) NOT NULL,
CONSTRAINT fk_book_id_media_id FOREIGN KEY (book_id, title) REFERENCES media (media_id, media_title)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE member_rents_media 
(
member_id INT NOT NULL,
media_id INT  NOT NULL,
PRIMARY KEY (member_id, media_id),
CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES university_member (member_id)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_media_id FOREIGN KEY (media_id) REFERENCES media (media_id)
ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE media_holds
(
member_id	INT NOT NULL,
media_id	INT NOT NULL,
CONSTRAINT holds_fk_member FOREIGN KEY (member_id) REFERENCES university_member(member_id)
ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT holds_fk_media FOREIGN KEY (media_id) REFERENCES media(media_id)
ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO library (location, library_name, affiliated_university) VALUES 
 ("Boston, MA", "Snell Library", "Northeastern University"), 
 ("New York, NY", "Bobst Library", "New York University"), 
 ("Los Angeles, CA", "Powell Library", "Univesity of California, Los Angeles");

 INSERT INTO university_member (member_last_name, member_first_name, street, city, state, zipcode, library_id) VALUES 
 ("Durant", "Kathleen", "360 Huntington Avenue", "Boston", "MA", 02115, 1),
 ("Bob", "Smith", "49 New York Street", "New York", "NY", 12345, 2),
 ("Carol", "Jones", "100 California Drive", "Los Angeles", "CA", 90348, 3),
 ("Alice", "Johnson", "330 Huntington Avenue", "Boston", "MA", 20118, 1); 

 INSERT INTO media (media_title) VALUES
 ("Harry Potter and the Chamber of Secrets"),
 ("Harry Potter and the Goblet of Fire"),
 ("Harry Potter Soundtrack"),
 ("Lord of the Rings: The Hobbit"),
 ("Harry Potter and the Chamber of Secrets");

 INSERT INTO book (book_id, title, author, pageCount, num_copies, genre, pubYear, availAsEbook, plot) VALUES
 (1, "Harry Potter and the Chamber of Secrets", "J.K Rowling", 341, 2, "Fantasy", 2002, true, "A mysterious elf tells Harry to expect trouble 
 during his second year at Hogwarts, but nothing can prepare him for trees that fight back, flying cars, spiders that talk and deadly 
 warnings written in blood on the walls of the school.");

 INSERT INTO video (video_id, title, director, actors, num_copies, genre, plot, runtime, year_released) VALUES
 (2, "Harry Potter and the Goblet of Fire", "John White", "Daniel Radcliffe, Emma Watson, Rupert Grint", 1, "Fantasy", "Harry Potter enters the Tri-Wizard tournament.", 157, 2005);

 INSERT INTO ebook (ebook_id, title, author, pageCount, num_copies,  genre, availInPrint, plot, printPubYear, ePubYear) VALUES
 (5, "Harry Potter and the Chamber of Secrets", "J.K Rowling", 341, 2, "Fantasy", true, "A mysterious elf tells Harry to expect trouble 
 during his second year at Hogwarts, but nothing can prepare him for trees that fight back, flying cars, spiders that talk and deadly 
 warnings written in blood on the walls of the school.", 2002, 2009);

INSERT INTO media_holds(media_id, member_id) VALUES 
(1, 1), (2,1), (5,1), (1, 2), (5, 2);

DELIMITER //

CREATE PROCEDURE delete_hold (
IN media_id INT, member_id INT
)

BEGIN 
    DELETE FROM media_holds 
    WHERE media_holds.media_id = media_id AND media_holds.member_id = member_id;
END //

DELIMITER ;
    
DELIMITER //

CREATE PROCEDURE place_hold (
IN mediaID INT, memberID INT
)

BEGIN 
    INSERT INTO media_holds VALUES
    (memberID, mediaID);

END //
DELIMITER ;

DELIMITER // 

-- figure out how to trigger the decrement of num_copies when 
-- a new tuple is added to member_rents_media

-- can update book, cd, ebook, or video 
CREATE TRIGGER decrement_book_copies
	AFTER INSERT ON member_rents_media
	FOR EACH ROW 
    
BEGIN
	UPDATE book
    SET num_copies = num_copies - 1
    WHERE book.book_id = NEW.media_id;
    
END // 

DELIMITER ; 

DELIMITER //

CREATE TRIGGER decrement_cd_copies
	AFTER INSERT ON member_rents_media
	FOR EACH ROW 
    
BEGIN
	UPDATE cd
    SET num_copies = num_copies - 1
    WHERE cd_id = NEW.media_id;
END // 

DELIMITER ; 

DELIMITER //

CREATE TRIGGER decrement_ebook_copies
	AFTER INSERT ON member_rents_media
	FOR EACH ROW 
    
BEGIN
	UPDATE ebook
    SET num_copies = num_copies - 1
    WHERE ebook_id = NEW.media_id;
END // 

DELIMITER ; 

DELIMITER //

CREATE TRIGGER decrement_video_copies
	AFTER INSERT ON member_rents_media
	FOR EACH ROW 
    
BEGIN
	UPDATE video
    SET num_copies = num_copies - 1
    WHERE video_id = NEW.media_id;
END // 

DELIMITER ; 



DELIMITER //

CREATE PROCEDURE add_rented_media (
IN memberID INT, mediaID INT
)

BEGIN 
INSERT INTO member_rents_media
VALUES (memberID, mediaID);

END //

DELIMITER ;




    
    
    
    
    
    
    
    
    
    
    
    
    
    

