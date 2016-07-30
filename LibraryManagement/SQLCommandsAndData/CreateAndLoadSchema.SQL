/*
*********************************************************
*		Library DB Database Create & Set up Commands	*
*	Prepared by SATHYANARAYANAN SRINIVASAN(sxs142031)	*
*			Revised as on OCT 25 2015					*
*														*
*   	Creates Schema & Loads the data from the CSVs	*
*	 	DO NOT ALTER or change the order of commands	*
*	 	The sequence is significant. 					*
*********************************************************
*/

DROP DATABASE IF EXISTS `Library.db`;
CREATE DATABASE  IF NOT EXISTS `Library.db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `library.db`;

/**** First  Create Tables for Library Data Import and set up - useful for setting up the actual schema *****/

DROP TABLE IF EXISTS `books_allcolumns`;
CREATE TABLE `books_allcolumns` (
  `ISBN10` varchar(10) NOT NULL,
  `ISBN13` varchar(13) DEFAULT NULL,
  `title` varchar(256) NOT NULL,
  `author` varchar(512) NOT NULL,
  `cover` varchar(512) DEFAULT NULL,
  `publisher` varchar(512) NOT NULL,
  `pages` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `book_copies_alldata`;
CREATE TABLE `book_copies_alldata` (
  `book_id` char(10) NOT NULL,
  `branch_id` int(11) NOT NULL,
  `no_of_copies` int(11) DEFAULT NULL COMMENT 'Table mapping books to branch; entire BOOK_COPIES_CLEAN.CSV is loaded for further processing'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**** Create Tables for Library Schema *****/

DROP TABLE IF EXISTS `borrower`;
CREATE TABLE `borrower` (
  `card_no` int(8) NOT NULL,
  `Fname` varchar(64) NOT NULL,
  `Lname` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  `address` varchar(128) NOT NULL,
  `city` varchar(24) NOT NULL DEFAULT 'UNKNOWN',
  `state` varchar(24) NOT NULL DEFAULT 'UNKNOWN',
  `phone` varchar(16) NOT NULL COMMENT 'Libraray Member''s information'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `library.db`.`borrower` 
ADD PRIMARY KEY (`card_no`)  COMMENT '',
ADD UNIQUE INDEX `card_no_UNIQUE` (`card_no` ASC)  COMMENT '',
ADD INDEX `FName` (`Fname` ASC)  COMMENT '',
ADD INDEX `LName` (`Lname` ASC)  COMMENT '',
ADD INDEX `Address` (`address` ASC)  COMMENT '',
ADD INDEX `Email` (`email` ASC)  COMMENT '',
ADD INDEX `Phone` (`phone` ASC)  COMMENT '';

DROP TABLE IF EXISTS `library_branch`;
CREATE TABLE `library_branch` (
  `branch_id` int(11) NOT NULL,
  `branch_name` varchar(64) NOT NULL,
  `branch_address` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `library.db`.`library_branch` 
ADD PRIMARY KEY (`branch_id`)  COMMENT '',
ADD UNIQUE INDEX `branch_id_UNIQUE` (`branch_id` ASC)  COMMENT '';

DROP TABLE IF EXISTS `book_loans`;
CREATE TABLE `book_loans` (
    `loan_id` INT(10) NOT NULL,
    `book_id` VARCHAR(10) NOT NULL,
    `branch_id` INT(11) NOT NULL,
    `card_no` INT(10) NOT NULL,
    `date_out` DATE DEFAULT NULL,
    `due_date` DATE DEFAULT NULL,
    `date_in` DATE NOT NULL default '0000-00-00'
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;
ALTER TABLE `library.db`.`book_loans` 
ADD PRIMARY KEY (`loan_id`)  COMMENT '',
ADD UNIQUE INDEX `loan_id_UNIQUE` (`loan_id` ASC)  COMMENT '';

DROP TABLE IF EXISTS `fines`;
CREATE TABLE `fines` (
  `loan_id` int(11) NOT NULL,
  `fine_amt` decimal(6,2) DEFAULT NULL,
  `paid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Fines paid by borrowers';
ALTER TABLE `library.db`.`fines` 
ADD PRIMARY KEY (`loan_id`)  COMMENT '',
ADD UNIQUE INDEX `loan_id_UNIQUE` (`loan_id` ASC)  COMMENT '';


DROP TABLE IF EXISTS `staff_users`;
CREATE TABLE `staff_users` (
  `rec_id` int(11) NOT NULL,
  `staff_firstname` varchar(45) NOT NULL,
  `staff_lastname` varchar(45) NOT NULL,
  `staff_user_id` varchar(16) NOT NULL,
  `staff_password` varchar(16) NOT NULL,
  `staff_email` varchar(45) DEFAULT NULL,
  `staff_secretkey` varchar(45) NOT NULL,
   PRIMARY KEY (`rec_id`),
   UNIQUE KEY `rec_id_UNIQUE` (`rec_id`),
   UNIQUE KEY `staff_user_id_UNIQUE` (`staff_user_id`),
   UNIQUE KEY `staff_email_UNIQUE` (`staff_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
/*
*****************************************************
*	Library DB Data Loading Commands				*
*	Prepared by SATHYANARAYANAN SRINIVASAN			*
*		Updated as on OCT 25 2015					*
*****************************************************
*/

/*
**	-----------------------------------------------------
**	(1) BOOKS DATA Table (**Import All Columns first **)
**	-----------------------------------------------------
*/
	TRUNCATE TABLE books_allcolumns;
	
	LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.6/Uploads/books3.csv' 
	INTO TABLE books_allcolumns 
	CHARACTER SET latin1
	FIELDS TERMINATED BY '	' 
	ENCLOSED BY ''
	LINES TERMINATED BY '\r\n'
	ignore 1 rows;

/*
**	-----------------------------------------------------
**	2) BOOKS_COPIES_ALLDATA Table 
**	-----------------------------------------------------
*/
	TRUNCATE TABLE book_copies_alldata;

	LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.6/Uploads/book_copies_clean.csv' 
	INTO TABLE book_copies_alldata
	FIELDS TERMINATED BY '	' 
	ENCLOSED BY ''
	LINES TERMINATED BY '\r\n'
	ignore 1 rows;

/*
**	-----------------------------------------------------
**	(3) BORROWER Table
**	-----------------------------------------------------
*/
	TRUNCATE TABLE borrower;
	
	LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.6/Uploads/borrowers_tabdelimited.csv' 
	INTO TABLE borrower 
	FIELDS TERMINATED BY '	' 
	ENCLOSED BY ''
	LINES TERMINATED BY '\n'
	ignore 1 rows;

/*
**	-----------------------------------------------------
**	(4) LIBRARY_BRANCH Table
**	-----------------------------------------------------
*/
	TRUNCATE TABLE library_branch;
	
	LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.6/Uploads/library_branch.csv' 
	INTO TABLE library_branch
	FIELDS TERMINATED BY '	' 
	ENCLOSED BY ''
	LINES TERMINATED BY '\n'
	ignore 1 rows;
	
/*
**	-----------------------------------------------------
**	(5) BOOK_LOANS Table
**	-----------------------------------------------------
*/
	TRUNCATE TABLE book_loans;
	
	LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 5.6/Uploads/book_loans_clean.csv' 
	INTO TABLE book_loans
	FIELDS TERMINATED BY '	' 
	ENCLOSED BY ''
	LINES TERMINATED BY '\r\n';
	
/*
***********************************************************************************
**  For the project following tables are created from the data so loaded from the *
**  base data .CSV files:														  *
***********************************************************************************
*/

/*
**	-----------------------------------------------------
**	(6) BOOK Table
**	-----------------------------------------------------
*/
	DROP TABLE IF EXISTS BOOK;

	create table IF NOT exists BOOK
		SELECT ISBN10 as 'book_id', title from books_allcolumns;
	ALTER TABLE `library.db`.`book` 
	ADD PRIMARY KEY (`book_id`)  COMMENT '',
	ADD UNIQUE INDEX `book_id_UNIQUE` (`book_id` ASC)  COMMENT '';


/*
**	-----------------------------------------------------
**	(6) BOOK_AUTHORS Table
**	-----------------------------------------------------
*/
	DROP TABLE IF EXISTS BOOK_AUTHORS;

	create table IF NOT exists BOOK_AUTHORS
		SELECT ISBN10 as 'book_id', AUTHOR as 'author_name' from books_allcolumns;

	ALTER TABLE `library.db`.`book_authors` 
	ADD PRIMARY KEY (`book_id`)  COMMENT '',
	ADD UNIQUE INDEX `book_id_UNIQUE` (`book_id` ASC)  COMMENT '';

	ALTER TABLE `library.db`.`book_authors` 
	ADD CONSTRAINT `BA_book_id`
	  FOREIGN KEY (`book_id`)
	  REFERENCES `library.db`.`book` (`book_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

/*
**	-----------------------------------------------------
**	(7) BOOK_COPIES Table
**	-----------------------------------------------------
*/
	DROP TABLE IF EXISTS BOOK_COPIES;

	CREATE TABLE IF NOT EXISTS BOOK_COPIES SELECT a.book_id,
		a.branch_id,
		a.no_of_copies,
		a.no_of_copies AS 'no_of_copies_available' FROM
		book_copies_alldata a,
		book b
	WHERE
		a.book_id = b.book_id
	GROUP BY a.book_id , a.branch_id;

	ALTER TABLE `library.db`.`book_copies` 
	ADD INDEX `book_id` (`book_id` ASC)  COMMENT '',
	ADD INDEX `branch_id` (`branch_id` ASC)  COMMENT '';

	ALTER TABLE `library.db`.`book_copies` 
	ADD CONSTRAINT `BC_book_id`
	  FOREIGN KEY (book_id)
	  REFERENCES `library.db`.`book` (book_id)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `BC_branch_id`
	  FOREIGN KEY (`branch_id`)
	  REFERENCES `library.db`.`library_branch` (`branch_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;


/*  COMPLETE THE SET UP BY ADDING THE REMAINING PRIMARY & FOREIGN KEYS AND CONSTRAINTS  */

	ALTER TABLE `library.db`.`book_loans` 
	ADD INDEX `book_id` (`book_id` ASC)  COMMENT '',
	ADD INDEX `branch_id` (`branch_id` ASC)  COMMENT '',
	ADD INDEX `card_no` (`card_no` ASC)  COMMENT '';
	ALTER TABLE `library.db`.`book_loans` 
	ADD CONSTRAINT `BL_book_id`
	  FOREIGN KEY (`book_id`)
	  REFERENCES `library.db`.`book` (`book_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `BL_branch_id`
	  FOREIGN KEY (`branch_id`)
	  REFERENCES `library.db`.`library_branch` (`branch_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `BL_card_no`
	  FOREIGN KEY (`card_no`)
	  REFERENCES `library.db`.`borrower` (`card_no`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

/* Insert test user in Staff_Users Table */	

	INSERT INTO `library.db`.`staff_users`
	(`rec_id`,
	`staff_firstname`,
	`staff_lastname`,
	`staff_user_id`,
	`staff_password`,
	`staff_email`,
	`staff_secretkey`)
	VALUES
	(1, 'Firstname-Test', 'Lastname-Test', 'test', '12345', 'test@test.com', '12345');
	  
	  
/*  ALL DONE   */
/*    END      */

