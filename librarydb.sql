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
year_released	INT,
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
 
 INSERT INTO library_staff (staff_name, street, city, state, zipcode, salary, library_id) VALUES 
 ("Jill Pill", "123 Willow Lane", "Boston", "MA", 02118, 15, 1),
 ("Jane McDonald", "44 Hyde Park", "Boston", "MA", 02115, 15, 1),
 ("Harry Sanders", "9 Wall Street", "New York", "NY", 11123, 15, 2),
 ("Martha Vineyard", "3345 Marble Street", "Torrance", "CA", 90223, 16, 3),
 ("Stewart Wrigely", "16 Broadway", "New York", "NY", 01123, 15, 2),
 ("Hannah Kansas", "12 Kansas City", "Santa Monica", "CA", 90274, 15, 3);

 INSERT INTO university_member (member_last_name, member_first_name, street, city, state, zipcode, library_id) VALUES 
 ("Durant", "Kathleen", "360 Huntington Avenue", "Boston", "MA", 02115, 1),
 ("Bob", "Smith", "49 New York Street", "New York", "NY", 12345, 2),
 ("Carol", "Jones", "100 California Drive", "Los Angeles", "CA", 90348, 3),
 ("Johnson", "Alice", "330 Huntington Avenue", "Boston", "MA", 20118, 1),
 ("White", "Duane", "443 South Side", "Bronx", "NY", 33340, 2),
 ("Arthur", "Maggie", "22222 Hanover Lane", "Waltham", "MA", 02117, 1),
 ("Show", "Sallie", "0032 Whitefield Avenue", "Santa Clara", "CA", 90878, 3),
 ("Jim", "John", "98 Firefly Street", "Azusza", "CA", 90328, 3),
 ("Liu", "Katherine", "38 Boston Park", "Boston", "MA", 20116, 1);

 INSERT INTO media (media_title) VALUES
("Harry Potter and the Chamber of Secrets"),
("Harry Potter and the Goblet of Fire"),
("Harry Potter Soundtrack"),
("Lord of the Rings: The Hobbit"),
("Harry Potter and the Chamber of Secrets"),
("Junie B. Jones and Her Big Fat Mouth"),
("All The Light We Cannot See"),
("All The Light We Cannot See"),
("Spiderman: Into the Spiderverse"),
("Now You See Me"),
("Frozen 2: Soundtrack"),
("Cardi B: The Album"), 
("Drip or Drown 2"),
("Frosty the Snowman"),
("Frosty the Snowman 2: The Snowman Returns"),
("The Polar Express Soundtrack"),
("Kafka on the Shore"),
("Brave New World"),
("1984"), 
("Lord of the Rings: The Hobbit");
 

 INSERT INTO book (book_id, title, author, pageCount, num_copies, genre, pubYear, availAsEbook, plot) VALUES
 (1, "Harry Potter and the Chamber of Secrets", "J.K Rowling", 341, 2, "Fantasy", 2002, true, "A mysterious elf tells Harry to expect trouble 
 during his second year at Hogwarts, but nothing can prepare him for trees that fight back, flying cars, spiders that talk and deadly 
 warnings written in blood on the walls of the school."), (4, "Lord of the Rings: The Hobbit", "J.R.R Tolkien", 304, 1, "Fantasy", 1937, false, "A hobbit 
 goes on a journey."), (6, "Junie B. Jones and Her Big Fat Mouth", "Barbara Park", 80, 6, "Realistic Fiction", 1992, false, "Junie B. Jones talks too much about 
 business that isn't hers."), (8, "All The Light We Cannot See", "Anthony Doerr", 544, 2, "Historical Fiction", 2014, true, "Blind girl and boy enlisted in war 
 during WWII share their experiences."), (17, "Kafka on the Shore", "Harumi Murakami", 603, 1, "Fiction", 2017, false, "Boy goes on journey; old man finds
talking cat.");

 INSERT INTO video (video_id, title, director, actors, num_copies, genre, plot, runtime, year_released) VALUES
 (2, "Harry Potter and the Goblet of Fire", "John White", "Daniel Radcliffe, Emma Watson, Rupert Grint", 1, "Fantasy", 
 "Harry Potter enters the Tri-Wizard tournament.", 157, 2005), (9, "Spiderman: Into the Spiderverse", "Peter Ramsey", 
 "Shameik Moore, Jake Johnson, Haliee Steinfeld, Mahershala Ali", 1, "Fantasy", "Animated film of Marvel's Spiderman on a new adventure.", 116, 2018),
 (10, "Now You See Me", "Louis Leterrier", "Isla Fisher, Dave Franco, Jessie Eisenberg, Woody Harrelson", 3, "Action", "Four magicians pull creative 
 stunts while confusing a detective relentlessly chasing them.", 115, 2013), (14, "Frosty the Snowman", "Johnson John", "Sally Mae, Victoria Secret, 
 Manny Osborn", 4, "Children", "A snowman comes to life", 103, 1997), (15, "Frosty the Snowman 2: The Snowman Returns", "Johnson Johnny", "Sallie May, 
 Victor Vincent, Hannah Ashley, Megan Markle", 5, "Horror", "A snowman comes back to life after he shouldve melted.", 114,  2000), (20, "Lord of the Rings: The Hobbit", 
 "Peter Jackson", "Andy Serkis, Fran Walsh, Philippa Boyens, Richard Taylor, Thomas Robin", 3, "Fantasy", "Wizard convinces hobbit to go on a quest.", 174, 2001);

 INSERT INTO ebook (ebook_id, title, author, pageCount, num_copies, genre, availInPrint, plot, printPubYear, ePubYear) VALUES
 (5, "Harry Potter and the Chamber of Secrets", "J.K Rowling", 341, 2, "Fantasy", true, "A mysterious elf tells Harry to expect trouble 
 during his second year at Hogwarts, but nothing can prepare him for trees that fight back, flying cars, spiders that talk and deadly 
 warnings written in blood on the walls of the school.", 2002, 2009), (7, "All The Light We Cannot See", "Anthony Doerr", 544, 3, "Historical Fiction", true, 
 "Blind girl and boy enlisted in army during WWII share their experiences.", 2014, 2015), (18, "Brave New World", "Aldous Huxley", 311, 8, "Science Fiction", 
 false, "Dystopian world", 1932, 2007), (19, "1984", "George Orwell", 328, 32, "Science Fiction", false, "Dystopian world where Big Brother is always 
watching", 1949, 2009);
 
 INSERT INTO cd (cd_id, album_name, artist, producer, genre, num_copies, year_released, song_list) VALUES 
 (3, "Harry Potter Soundtrack", "J. K. Rowling II", "London on the Track", "Alternative, Hip-hop", 2, 2002, "Harry Goes to Hogwarts, Meeting Ron and Herminonie, I Want to Fly, 
 We Won the Quidditch Match"), (11, "Frozen 2: Soundtrack", "Idina Menzel", "Rick Rubin", "Pop", 1, 2013, "Let It Go Pt. 2, Sandwiches, Olaf's Nose, 
 The Reindeer, We're Sisters"), (12, "Cardi B: The Album", "Cardi B", "Offset", "Hip-hop", 53, 2017, "Money, I Like It, Ring, Rodeo, Bartier Cardi"),
(13, "Drip or Drown 2", "Gunna", "ChaseTheMoney", "Hip-hop", 12, 2016, "Drip or Drown, Outta Sight Outta Mind, Outstanding, One Call, Cash War"),
(16, "The Polar Express Soundtrack", "Billy Joel", "Lighthouse Studios", "Soft Rock", 6, 2000, "Polar Express is Coming, Little Boy Blue, Winter is Here");

INSERT INTO media_holds(member_id, media_id) VALUES 
(1, 1), (2,1), (5,1), (1, 2), (5, 8), (1, 6), (9, 3), (5, 3), (8, 5), (8, 8), (6, 13), (7, 13), (5, 13), (4, 12), (2, 12), (9, 16), (9, 10);

INSERT INTO member_rents_media (member_id, media_id) VALUES
(1, 10), (3, 13), (1, 13), (1, 16), (2, 10), (4, 16), (8, 16), (9, 19), (5, 15), (4, 7) (7, 19), (2, 19), (8, 19)

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




    
    
    
    
    
    
    
    
    
    
    
    
    
    

