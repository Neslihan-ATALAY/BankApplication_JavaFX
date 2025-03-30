package main.java.com.neslihanatalay.BankApp.controller;

import main.java.com.neslihanatalay.BankApp.dto.UserDTO;
import main.java.com.neslihanatalay.BankApp.dao.UserDAO;
import main.java.com.neslihanatalay.BankApp.utils.SceneHelper;
import main.java.com.neslihanatalay.BankApp.utils.SpecialColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.Optional;

public class LoginController {

    private LoginDAO loginDAO;

    public LoginController() {
        loginDAO = new LoginDAO();
    }

    @FXML
    private TextField TCNumberField;
    @FXML
    private TextField passwordField;

    @FXML
    public void login() {
        String TCNumber = "", password = "";
        TCNumber = TCNumberField.getText().trim();
        password = passwordField.getText().trim();

	      if (TCNumber == "" || password == "") {
	          loginDAO.showAlert("Uyarı", "Sisteme girişte TC Kimlik Numarası ve Şifre alanları doldurulmalıdır, lütfen giriş işleminizi tekrar deneyiniz.", Alert.WARNING);
	          loginDAO.switchToPage(FXMLPath.LOGIN, "Giriş");
	      }

        Optional<UserDTO> optionalLoginUserDTO = loginDAO.loginUser(TCNumber, password);

        if (optionalLoginUserDTO.isPresent()) {
            UserDTO userDTO = optionalLoginUserDTO.get();
			      loginDAO.setLoginUserId(userDTO.getId());
            loginDAO.showAlert("Bilgi", "Giriş Başarılı", Alert.AlertType.INFORMATION);
	          loginDAO.switchToPage(FXMLPath.INDEX, "Anasayfa");
        } else {
            loginDAO.showAlert("Hata", "Giriş Başarısız", Alert.AlertType.ERROR);
            loginDAO.switchToPage(FXMLPath.LOGIN, "Giriş");
	      }
    }

    @FXML
    public void switchToPage(String fxmlPath, String title) {
		    accountDAO.switchToPage(fxmlPath, title);
    }
}
