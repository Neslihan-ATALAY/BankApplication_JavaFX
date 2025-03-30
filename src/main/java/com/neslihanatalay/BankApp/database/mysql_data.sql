CREATE DATABASE BankDb;

USE BankDb;

CREATE TABLE IF NOT EXISTS banks (
	id INT AUTO_INCREMENT PRIMARY KEY,
	bankName NVARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS transactionTypes (
	id INT AUTO_INCREMENT PRIMARY KEY,
	transactionType NVARCHAR(200) NOT NULL
);
CREATE TABLE IF NOT EXISTS users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name NVARCHAR(100) NOT NULL,
	surname NVARCHAR(100) NOT NULL,
	password NVARCHAR(255) NOT NULL,            		
	TCNumber VARCHAR(11) NOT NULL UNIQUE,
	address NVARCHAR(300),
	createDate DATETIME,
	updateDate DATETIME
);
CREATE TABLE IF NOT EXISTS accounts (
	id INT AUTO_INCREMENT PRIMARY KEY,
	userId INT NOT NULL,
	ibanAccountNumber VARCHAR(24) NOT NULL,
	moneyQuantity DECIMAL NOT NULL,
	createDate DATETIME,
	updateDate DATETIME
);
CREATE TABLE IF NOT EXISTS transactions (
	id INT AUTO_INCREMENT PRIMARY KEY,
	transactionTypeId INT NOT NULL,
	accountId INT NOT NULL,
	toAccountId INT,
	moneyQuantity DECIMAL NOT NULL,
	toNameSurname NVARCHAR(200),
	toBankId INT,
	toIbanAccountNumber VARCHAR(24),
	description NVARCHAR(500),
	transactionDate DATETIME
);

INSERT INTO banks (id, bankName) VALUES (1, "X Bankası");
INSERT INTO banks (id, bankName) VALUES (2, "Y Bankası");
INSERT INTO banks (id, bankName) VALUES (3, "Z Bankası");
INSERT INTO transactionTypes (id, transactionType) VALUES (1, "Hesaptan Para Çekme");
INSERT INTO transactionTypes (id, transactionType) VALUES (2, "Hesaba Para Yatırma");
INSERT INTO transactionTypes (id, transactionType) VALUES (3, "Başka Hesaba Para Gönderme");
INSERT INTO users (name, surname, password, TCNumber, address, createDate, updateDate)
VALUES ("İSİM", "SOYİSİM", "root", "11111111111", "Mahalle Cadde Sokak İlçe - İl - Ülke", "29-03-2025 13:00:00", "29-03-2025 13:00:00");

select * FROM users;
--SELECT  *  FROM users WHERE name="İSİM";
--UPDATE users SET name="İSİMM", password="1234" WHERE id=1;
--DELETE FROM users  WHERE id=1;
