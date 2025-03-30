package com.neslihanatalay.BankApp.dao;

import com.neslihanatalay.BankApp.database.SingletonDBConnection;
import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface IDaoImplements<T> extends ICrud<T>, IGenericsMethod<T>, ILogin<T> {
    default Connection iDaoImplementsDatabaseConnection() {
        return SingletonDBConnection.getInstance().getConnection();
	  }
  
    public LocalDateTime dateAndTimeNow() {
        /*
        Locale locale = new Locale("tr", "TR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", locale);
        return LocalDateTime.now().format(formatter);
        */
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", new Locale("tr", "TR")));
    }

    @FXML
    public void switchToPage(String fxmlPath, String title) {
        try {
            SceneHelper.switchScene(fxmlPath, title);
        } catch (Exception e) {
            System.out.println(SpecialColor.RED + "Sayfaya Yönlendirilemedi" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Sayfa Yüklenemedi", Alert.AlertType.ERROR);
        }
    }

    public void showAlert(String title, String message, Alert.AlertType type) {
		Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void specialOnEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            switchToPage(FXMLPath.INDEX, "Anasayfa");
        }
    }
}
