DROP DATABASE IF EXISTS bank;
CREATE DATABASE bank;

USE bank;
Drop table if exists Customer_account;
Drop table if exists Customers;
Drop table if exists Accounts;

Create table Customers(
    CustomerID int NOT NULL auto_increment primary key,
    FirstName varchar(50),
    LastName varchar(50),
    SIN varchar(9),
    Password varchar(20))
    Engine=InnoDB;
    
Create table Accounts(
    AccountID int NOT NULL auto_increment primary key,
    AccType varchar(50),
    Balance double)
    Engine=InnoDB;
    
Create table Customer_account(
    ID int NOT NULL auto_increment primary key,
    CustomerID int,
    AccountID int,
    foreign key (CustomerID) references Customers(CustomerID) on delete cascade,
    foreign key (AccountID) references Accounts(AccountID) on delete cascade)
    Engine=InnoDB;