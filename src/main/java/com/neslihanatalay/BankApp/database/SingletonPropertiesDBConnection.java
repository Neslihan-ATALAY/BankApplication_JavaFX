package main.java.com.neslihanatalay.BankApp.database;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
public class SingletonPropertiesDBConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static SingletonPropertiesDBConnection instance;
    private Connection connection;

    private SingletonPropertiesDBConnection() {
        try {
            loadDatabaseConfig();
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Veritabanı bağlantısı başarılı");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı bağlantısı başarısız!");
        }
    }

    private static void loadDatabaseConfig() {
        try (FileInputStream input = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            URL = properties.getProperty("db.url", "jdbc:mysql://localhost:3306/BankDb?useSSL=false";");
            USERNAME = properties.getProperty("db.username", "root");
            PASSWORD = properties.getProperty("db.password", "");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı yapılandırması yüklenemedi!");
        }
    }

    public static synchronized SingletonPropertiesDBConnection getInstance() {
        if (instance == null) {
            instance = new SingletonPropertiesDBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            } catch (SQLException e) {
                throw new RuntimeException("Bağlantı kapatılırken hata oluştu!", e);
            }
        }
    }
}
