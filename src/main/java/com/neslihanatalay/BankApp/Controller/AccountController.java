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
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class AccountController {

    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;

    private Random random;
    private final Integer count = 24;

    @FXML
    private Label welcomeText = "";
    @FXML
    private Label userIdField;
    @FXML
    private Combobox<Integer> accountIdField;
    @FXML
    private TextField ibanAccountNumberField;
    @FXML
    private TextField moneyQuantityField;
    @FXML
    private TextField descriptionField;

    public AccountController() {
        accountDAO = new AccountDAO();
		transactionDAO = new TransactionDAO();
		userDAO = new UserDAO();
		random = new Random();
		String text = "\nHoşgeldiniz ";
		Optional<UserDTO> userDTO = userDAO.findById(userDAO.getLoginUserId());
		text += userDTO.getName().toUpperCase();
		text += " ";
		text += userDTO.getSurname().toUpperCase();
		welcomeText.setText(text);
    }

    @FXML
    public void accountRegister() {
        Integer userId = 0;
        String ibanAccountNumber;
        LocalDateTime createDate, updateDate;
		userId = getLoginUserId();
		ibanAccountNumber = "TR";
		for (int i = 0; i < count; i++) {
			ibanAccountNumber += random.next(0, 9).ToString();
		}        
        Locale locale = new Locale("tr", "TR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", locale);
        LocalDateTime now = LocalDateTime.now().format(formatter);
        createDate = now;
        updateDate = now;
	
		if (userId == 0) {
			userDAO.showAlert("Hata", "Kullanıcı oturumunda bir sorun meydana geldi, lütfen sisteme tekrar giriş yapınız.", Alert.AlertType.ERROR);
			userDAO.switchToPage(FXMLPath.LOGIN, "Giriş");
		}	
		if (ibanAccountNumber == "" || ibanAccountNumber.isEmpty() || ibanAccountNumber == null) {
			userDAO.showAlert("Hata", "Yeni hesapta bir sorun meydana geldi, lütfen işlemi tekrar deneyiniz.", Alert.AlertType.ERROR);
			userDAO.switchToPage(FXMLPath.ACCOUNTREGISTER, "Yeni Hesap");
		}

        Optional<AccountDTO> optionalRegisterAccountDTO = Optional.ofNullable(AccountDTO.builder()
                //.id(0)
                .userId(userId)
                .ibanAccountNumber(ibanAccountNumber)
				.moneyQuantity(0.0)
                .createDate(createDate)
                .updateDate(updateDate)
                .build());

        if (optionalRegisterAccountDTO.isPresent()) {
            Optional<AccountDTO> createdAccountDTO = accountDAO.create(optionalRegisterAccountDTO.get());
            if (createdAccountDTO.isPresent()) {
                accountDAO.showAlert("Bilgi", "Hesap Kaydı Başarılı", Alert.AlertType.INFORMATION);
                accountDAO.switchToPage(FXMLPath.INDEX, "Anasayfa");
            } else {
                accountDAO.showAlert("Uyarı", "Hesap Kaydı Başarısız", Alert.AlertType.ERROR);
                accountDAO.switchToPage(FXMLPath.ACCOUNTREGISTER, "Yeni Hesap");
			}
        }
    }

    @FXML
    public void accountSetMoney() {
        Integer userId = 0, accountId = 0;
        String ibanAccountNumber, description;
		Double moneyQuantity = 0.0, fieldMoneyQuantity = 0.0, newMoneyQuantity = 0.0;
        LocalDateTime createDate, updateDate;
		userId = accountDAO.getLoginUserId();
		if (userId == 0) {
			userDAO.showAlert("Hata", "Kullanıcı oturumunda bir sorun meydana geldi, lütfen sisteme tekrar giriş yapınız.", Alert.AlertType.ERROR);
			userDAO.switchToPage(FXMLPath.LOGIN, "Giriş");
		}
		Optional<AccountDTO> accountDTO = accountDAO.findByUserId(userId);
		if (accountDTO.isPresent())
		{
            accountId = accountDTO.getId();
			moneyQuantity = accountDTO.getMoneyQuantity();
			ibanAccountNumber = accountDTO.getIbanAccountNumber();
			fieldMoneyQuantity = Double.valueOf(moneyQuantityField.getText());
			if ((ibanAccountNumber == "" || ibanAccountNumber.isEmpty() || ibanAccountNumber == null) && moneyQuantity == 0.0) {
				userDAO.showAlert("Uyarı", "Hesap bilgileriniz boş geçilemez, lütfen hesap bilgilerinizi güncelleyerek hesabınızı tekrar seçiniz.", Alert.AlertType.WARNING);
				userDAO.switchToPage(FXMLPath.ACCOUNTUPDATE, "Hesap Güncelleme");
			}
			if (fieldMoneyQuantity == 0.0 || fieldMoneyQuantity.isEmpty()) {
				userDAO.showAlert("Uyarı", "Hesabınıza yatırmak istediğiniz para miktarını sıfırdan farklı giriniz, miktar alanını boş geçmeyiniz, işleminizi tekrar deneyiniz.", Alert.AlertType.WARNING);
				userDAO.switchToPage(FXMLPath.ACCOUNTSETMONEY, "Para Yatırma");
			}
			newMoneyQuantity = (moneyQuantity + fieldMoneyQuantity);
			createDate = Integer.valueOf(accountDTO.getCreateDate());
			Locale locale = new Locale("tr", "TR");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", locale);
			LocalDateTime now = LocalDateTime.now().format(formatter);
			updateDate = now;

			Optional<AccountDTO> optionalAccountDTO = Optional.ofNullable(AccountDTO.builder()
				.id(accountId)
				.userId(userId)
				.ibanAccountNumber(ibanAccountNumber)
				.moneyQuantity(newMoneyQuantity)
				.createDate(createDate)
				.updateDate(updateDate)
				.build());

			if (optionalAccountDTO.isPresent()) {
				Optional<AccountDTO> accountSetMoneyDTO = accountDAO.update(optionalAccountDTO.get());
				if (accountSetMoneyDTO.isPresent()) {
					description = descriptionField.getText().trim();
					now = LocalDateTime.now().format(formatter);
					Optional<TransactionDTO> transactionDTO = Optional.ofNullable(TransactionDTO.builder()
						.transactionTypeId(2)
						.accountId(accountId)
						.moneyQuantity(fieldMoneyQuantity)
						.description(description)
						.transactionDate(now)
						.build());
					Optional<TransactionDTO> createdTransactionDTO = transactionDAO.create(transactionDTO.get());
					if (createdTransaction.isPresent()) {
						accountDAO.showAlert("Bilgi", "Para Hesaba Kaydedildi", Alert.AlertType.INFORMATION);
						accountDAO.switchToPage(FXMLPath.INDEX, "Anasayfa");
					}
				} else {
					accountDAO.showAlert("Uyarı", "Para Hesaba Kaydedilemedi", Alert.AlertType.ERROR);
					accountDAO.switchToPage(FXMLPath.ACCOUNTSETMONEY, "Para Yatırma");
				}
			}
		} else {
			accountDAO.showAlert("Hata", "Para yatırma işleminde bir sorun meydana geldi, lütfen işleminizi tekrar deneyiniz.", Alert.AlertType.ERROR);
			accountDAO.switchToPage(FXMLPath.ACCOUNTSETMONEY, "Para Yatırma"));
		}
	}
		
    @FXML
    public void accountGetMoney() {
        Integer userId = 0, accountId = 0;
        String ibanAccountNumber, description;
		Double moneyQuantity = 0.0, fieldMoneyQuantity = 0.0, newMoneyQuantity = 0.0;
        LocalDateTime createDate, updateDate;
		userId = accountDAO.getLoginUserId();
		if (userId == 0) {
			userDAO.showAlert("Hata", "Kullanıcı oturumunda bir sorun meydana geldi, lütfen sisteme tekrar giriş yapınız.", Alert.AlertType.ERROR);
			userDAO.switchToPage(FXMLPath.LOGIN, "Giriş");
		}
		Optional<AccountDTO> accountDTO = accountDAO.findByUserId(userId);
        if(accountDTO.isPresent()) {
			accountId = Integer.valueOf(accountDTO.getId());
			ibanAccountNumber = accountDTO.getIbanAccountNumber();
			moneyQuantity = accountDTO.getMoneyQuantity();
			fieldMoneyQuantity = Double.valueOf(moneyQuantityField.getText());
			if (ibanAccountNumber == "" && moneyQuantity == 0.0) {
				userDAO.showAlert("Uyarı", "Hesap bilgileriniz boş geçilemez, lütfen hesap bilgilerinizi güncelleyerek hesabınızı tekrar seçiniz.", Alert.AlertType.WARNING);
				userDAO.switchToPage(FXMLPath.ACCOUNTUPDATE, "Hesap Güncelleme");
			}
			if (fieldMoneyQuantity == 0.0 || fieldMoneyQuantity.isEmpty()) {
				userDAO.showAlert("Uyarı", "Hesabınızdan çekmek istediğiniz para miktarını sıfırdan farklı giriniz, miktar alanını boş geçmeyiniz, lütfen işleminizi tekrar deneyiniz.", Alert.AlertType.WARNING);
				userDAO.switchToPage(FXMLPath.ACCOUNTGETMONEY, "Para Yatırma");
			}
			if (fieldMoneyQuantity > moneyQuantity) {
				userDAO.showAlert("Uyarı", "Hesabınızdaki para miktarı yeterli değildir, lütfen işleminizi tekrar deneyiniz. Hesabınızdaki para miktarı: " + moneyQuantity + " TL", Alert.AlertType.WARNING);
				userDAO.switchToPage(FXMLPath.ACCOUNTGETMONEY, "Para Yatırma");
			}
			newMoneyQuantity = (moneyQuantity - fieldMoneyQuantity);
			createDate = Integer.valueOf(accountDTO.getCreateDate());
            Locale locale = new Locale("tr", "TR");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", locale);
            LocalDateTime now = LocalDateTime.now().format(formatter);
			updateDate = Integer.valueOf(now);

			Optional<AccountDTO> optionalAccountDTO = Optional.ofNullable(AccountDTO.builder()
                .id(accountId)
                .userId(userId)
				.ibanAccountNumber(ibanAccountNumber)
				.moneyQuantity(newMoneyQuantity)
				.createDate(createDate)
                .updateDate(updateDate)
                .build());
			if (optionalAccountDTO.isPresent()) {
				Optional<AccountDTO> accountGetMoneyDTO = accountDAO.update(optionalAccountDTO.get());
				if (accountGetMoneyDTO.isPresent()) {
					description = descriptionField.getText().trim();
					now = LocalDateTime.now().format(formatter);
					Optional<TransactionDTO> transactionDTO = Optional.ofNullable(TransactionDTO.builder()
						.transactionTypeId(1)
						.accountId(accountId)
						.moneyQuantity(fieldMoneyQuantity)
						.description(description)
						.transactionDate(now)
						.build());
					Optional<TransactionDTO> createdTransactionDTO = transactionDAO.create(transactionDTO.get());
                    if (createdTransaction.isPresent()) {
                    	accountDAO.showAlert("Bilgi", "Para Hesaptan Alındı", Alert.AlertType.INFORMATION);
                    	accountDAO.switchToPage(FXMLPath.INDEX, "Anasayfa");
                    }
				} else {
					accountDAO.showAlert("Uyarı", "Para Hesaptan Alınamadı", Alert.AlertType.ERROR);
					accountDAO.switchToPage(FXMLPath.ACCOUNTGETMONEY, "Para Çekme"));
				}
            }
    	} else {
			accountDAO.showAlert("Hata", "Para çekme işleminde bir sorun meydana geldi, lütfen işleminizi tekrar deneyiniz.", Alert.AlertType.ERROR);
			accountDAO.switchToPage(FXMLPath.ACCOUNTGETMONEY, "Para Çekme"));
		}
    }

    @FXML
    public void switchToPage(String fxmlPath, String title) {
		accountDAO.switchToPage(fxmlPath, title);
    }
}
