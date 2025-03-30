package main.java.com.neslihanatalay.BankApp.controller;

import main.java.com.neslihanatalay.BankApp.dao.UserDAO;
import main.java.com.neslihanatalay.BankApp.dto.UserDTO;
import main.java.com.neslihanatalay.BankApp.utils.SceneHelper;
import main.java.com.neslihanatalay.BankApp.utils.SpecialColor;
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
import java.util.Optional;

public class UserController {

    private UserDAO userDAO;

    public UserController() {
        userDAO = new UserDAO();
    }

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField TCNumberField;
    @FXML
    private TextField addressField;

    @FXML
    public void userRegister() {
        String name, surname, password, TCNumber, address;
        LocalDateTime createDate, updateTime, now;
	name = nameField.getText().trim();
        surname = surnameField.getText().trim();
        password = passwordField.getText().trim();
        TCNumber = TCNumberField.getText().trim();
        address = addressField.getText().trim();
	now = dateAndTimeNow();
	createDate = now;
	updateDate = now;

	if (name != "" && surname != "" && password != "" && TCNumber != "") {
	    userDAO.showAlert("Uyarı", "Ad, soyad, şifre ve TC kimlik numarası alanlarını doldurmak zorunludur, alanları boş bırakmayınız", Alert.WARNING);
	    userDAO.switchToPage(FXMLPath.USERREGISTER, "Yeni Kullanıcı");
	}
	Optional<UserDTO> findUserDTO = userDAO.findByTCNumber(TCNumber);
	if (findUserDTO.isPresent()) {
	    userDAO.showAlert("Uyarı", "TC Kimlik Numarası Sistemde Kayıtlı, TC Kimlik Numaranız ve Şifreniz ile Uygulamaya Giriş Yapabilirsiniz.", Alert.WARNING);
	    userDAO.switchToPage(FXMLPath.USERREGISTER, "Yeni Kullanıcı");
	}

        Optional<UserDTO> optionalRegisterUserDTO = Optional.ofNullable(UserDTO.builder()
                .id(0)
                .name(name)
                .surname(surname)
                .password(password)
                .TCNumber(TCNumber)
                .address(address)
		.createDate(createDate)
		.updateDate(updateDate)
                .build());

        if (optionalRegisterUserDTO.isPresent()) {
            Optional<UserDTO> createdUserDTO = userDAO.create(optionalRegisterUserDTO.get());

            if (createdUserDTO.isPresent()) {
                userDAO.showAlert("Bilgi", "Kullanıcı Kaydı Başarılı", Alert.AlertType.INFORMATION);
                userDAO.switchToPage(FXMLPath.INDEX, "Anasayfa");
            } else {
                userDAO.showAlert("Uyarı", "Kullanıcı Kaydı Başarısız", Alert.AlertType.ERROR);
                userDAO.switchToPage(FXMLPath.USERREGISTER, "Yeni Kullanıcı");
			}
        }
    }

    @FXML
    public void switchToPage(String fxmlPath, String title) {
	accountDAO.switchToPage(fxmlPath, title);
    }
}
