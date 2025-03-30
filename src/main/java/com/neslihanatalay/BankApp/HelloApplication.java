package com.neslihanatalay.BankApp;

import com.neslihanatalay.BankApp.database.SingletonPropertiesDBConnection;
import com.neslihanatalay.BankApp.utils.FXMLPath;
import com.neslihanatalay.BankApp.utils.SceneHelper;
import com.neslihanatalay.BankApp.utils.SpecialColor;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        dataSet();
	      SceneHelper.switchScene(FXMLPath.LOGIN, "Giriş");
	/*
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setTitle("Login Sayfası");
        stage.setScene(new Scene(parent));
        stage.show();
	*/
    }

    public static void dataSet() throws SQLException {
        Connection connection = SingletonPropertiesDBConnection.getInstance().getConnection();

        try (Statement stmt = connection.createStatement()) {
            // User-Kullanıcı tablosu
            String createUserTableSQL = """
        	CREATE TABLE IF NOT EXISTS users (
            		id INT AUTO_INCREMENT PRIMARY KEY,
            		name NVARCHAR(100) NOT NULL,
            		surname NVARCHAR(100) NOT NULL,
			email NVARCHAR(150),
            		password NVARCHAR(255) NOT NULL,            		
			TCNumber VARCHAR(11) NOT NULL UNIQUE,
			telephoneNumber NVARCHAR(50),
			city NVARCHAR(50),
			address NVARCHAR(300),
			createDate DATETIME,
			updateDate DATETIME
        	);
    		""";
            stmt.execute(createUserTableSQL);

            // Hesap-Account tablosu
            String createAccountTableSQL = """
        	CREATE TABLE IF NOT EXISTS accounts (
            		id INT AUTO_INCREMENT PRIMARY KEY,
            		userId INT NOT NULL,
			accountTypeId INT NOT NULL,
			ibanAccountNumber VARCHAR(24) NOT NULL,
			moneyQuantity DECIMAL,
			moneyTypeId INT NOT NULL,
			cardTypeId INT,
			cardNumber VARCHAR(19),
			cardLastMonth INT,
			cardLastYear INT,
			cardCSV VARCHAR(3),
			createDate DATETIME,
			updateDate DATETIME
        	);
    		""";
            stmt.execute(createAccountTableSQL);

	    // İşlem-Transaction tablosu
            String createTransactionTableSQL = """
        	CREATE TABLE IF NOT EXISTS transactions (
            		id INT AUTO_INCREMENT PRIMARY KEY,
            		transactionTypeId INT NOT NULL,
			accountId INT NOT NULL,
			toAccountId INT,
			moneyQuantity DECIMAL,
			moneyTypeId INT NOT NULL,
			toNameSurname NVARCHAR(200),
			toBankId INT,
			toIbanAccountNumber VARCHAR(24),
			description NVARCHAR(500),
			transactionDate DATETIME
        	);
    		""";
            stmt.execute(createTransactionTableSQL);

	    String createBankTableSQL = """
        	CREATE TABLE IF NOT EXISTS banks (
            		id INT AUTO_INCREMENT PRIMARY KEY,
            		bankName NVARCHAR(100) NOT NULL
        	);
    		""";
            stmt.execute(createUserTableSQL);

	    String createTransactionTypeTableSQL = """
        	CREATE TABLE IF NOT EXISTS transactionTypes (
            		id INT AUTO_INCREMENT PRIMARY KEY,
            		transactionType NVARCHAR(200) NOT NULL
        	);
    		""";
            stmt.execute(createUserTableSQL);
        }

	/*

        String insertSQL = """
            INSERT INTO users (name, surname, email, password, TCNumber, telephoneNumber, city, address, createDate, updateDate)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

	Locale locale = new Locale("tr", "TR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", locale);
	LocalDateTime now = LocalDateTime.now().format(formatter);

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            // kullanıcı
            ps.setString(1, "İSİM");
            ps.setString(2, "SOYİSİM");
            ps.setString(3, "email@email.com");
            ps.setString(4, BCrypt.hashpw("root", BCrypt.gensalt()));
            ps.setString(5, "11111111111");
            ps.setString(6, "05555555555");
            ps.setString(7, "ŞEHİR");
            ps.setString(8, "Mahalle Cadde Sokak İlçe - İl - Ülke");
            ps.setString(9, now);
            ps.setString(10, now);
            ps.executeUpdate();
        }

        System.out.println("✅ Kullanıcılar başarıyla eklendi.");
	*/
    }
    
    public static void main(String[] args) {
        launch();
    }
}
