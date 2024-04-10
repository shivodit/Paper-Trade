CREATE DATABASE paper_trade;
CREATE TABLE User (
    User_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Name VARCHAR(100) NOT NULL,
    DOB DATE NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(50) NOT NULL,
    Balance DECIMAL(20,3) NOT NULL DEFAULT 100000,
    PRIMARY KEY (User_ID)
);

CREATE TABLE Contact (
    User_ID INT NOT NULL,
    Phone_no VARCHAR(20) NOT NULL PRIMARY KEY,
    FOREIGN KEY (User_ID) REFERENCES User(User_ID)
);

CREATE TABLE Stock (
    Symbol VARCHAR(25) NOT NULL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL
);

CREATE TABLE StockOrder (
    Order_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Price DECIMAL NOT NULL,
    Order_type ENUM('buy', 'sell') NOT NULL,
    Quantity INT NOT NULL,
    Order_class ENUM('market', 'limit') NOT NULL,
    Time TIMESTAMP NOT NULL,
    Order_status ENUM('active', 'executed', 'cancelled') NOT NULL,
    User_ID INT NOT NULL,
    Symbol VARCHAR(25) NOT NULL,
    PRIMARY KEY (Order_ID),
    FOREIGN KEY (User_ID) REFERENCES User(User_ID),
    FOREIGN KEY (Symbol) REFERENCES Stock(Symbol)
);

CREATE TABLE Stock_Price (
    Symbol VARCHAR(25) NOT NULL,
    Timestamp TIMESTAMP NOT NULL,
    Open DECIMAL(10,3) NOT NULL,
    Close DECIMAL(10,3) NOT NULL,
    High DECIMAL(10,3) NOT NULL,
    Low DECIMAL(10,3) NOT NULL,
    PRIMARY KEY (Symbol, Timestamp),
    FOREIGN KEY (Symbol) REFERENCES Stock(Symbol)
);

CREATE TABLE Watchlist (
    User_ID INT NOT NULL,
    Watchlist_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    List_Name VARCHAR(50) NOT NULL DEFAULT 'Watchlist',
    PRIMARY KEY (Watchlist_ID),
    FOREIGN KEY (User_ID) REFERENCES User(User_ID)
);

CREATE TABLE Tutorial (
    Tutorial_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Hyperlink VARCHAR(255) NOT NULL,
    Title VARCHAR(50) NOT NULL,
    Description VARCHAR(600),
    PRIMARY KEY (Tutorial_ID)
);

CREATE TABLE Holds (
    Symbol VARCHAR(25) NOT NULL,
    User_ID INT NOT NULL,
    Avg_price DECIMAL(10,3) NOT NULL,
    Quantity INT NOT NULL,
    PRIMARY KEY (Symbol, User_ID),
    FOREIGN KEY (Symbol) REFERENCES Stock(Symbol),
    FOREIGN KEY (User_ID) REFERENCES User(User_ID)
);

CREATE TABLE Tracks (
    Symbol VARCHAR(25) NOT NULL,
    Watchlist_ID INT NOT NULL,
    PRIMARY KEY (Symbol, Watchlist_ID),
    FOREIGN KEY (Symbol) REFERENCES Stock(Symbol),
    FOREIGN KEY (Watchlist_ID) REFERENCES Watchlist(Watchlist_ID)
);

CREATE TABLE Takes (
    User_ID INT NOT NULL,
    Tutorial_ID INT NOT NULL,
    Rating TINYINT CHECK (Rating >= 0 AND Rating <= 5),
    PRIMARY KEY (User_ID, Tutorial_ID),
    FOREIGN KEY (User_ID) REFERENCES User(User_ID),
    FOREIGN KEY (Tutorial_ID) REFERENCES Tutorial(Tutorial_ID)
);

use paper_trade;
show tables;