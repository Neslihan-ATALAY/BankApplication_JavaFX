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

public class TransactionController {

    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;

    @FXML
    private ComboBox<Integer> transactionTypeIdField;
    @FXML
    private ComboBox<Integer> accountIdField;
    @FXML
    private ComboBox<Integer> toAccountIdField;
    @FXML
    private TextField moneyQuantityField;
    @FXML
    private TextField toNameSurnameField;
    @FXML
    private ComboBox<Integer> toBankIdField;
    @FXML
    private TextField toIbanAccountNumberField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField transactionDateField;

    public TransactionController() {
        transactionDAO = new TransactionDAO();
		accountDAO = new AccountDAO();
		userId = transactionDAO.getLoginUserId();
		if (userId != 0) {
			Optional<AccountDTO> accountDTO = accountDAO.findByUserId(userId);
			if (accountDTO.isPresent())
			{
				accountId = accountDTO.getId();
				Optional<List<AccountDTO>> listAccounts = transactionDAO.listByAccountId(accountId);
				if (listAccounts.length > 0) {
					toAccountIdField.getItem[i].setValue(0);
					toAccountIdField.getItem[i].setText("SEÇİNİZ");
            	    for (int i = 0; i < listAccounts.length; i++) {
						toAccountIdField.getItems().add(listAccounts.get(i).getId());
						toAccountIdField.getItem[i].setValue(listAccounts.get(i).getId());
						toAccountIdField.getItem[i].setText(listAccounts.get(i).getIbanAccountNumber());
				}
			}
	    }
		Optional<List<BankDTO>> listBanks = transactionDAO.listBanks();
		if (listBanks.length > 0)
		{
			if (listBanks.length > 0) {
			bankIdField.getItem[i].setValue(0);
			bankIdField.getItem[i].setText("SEÇİNİZ");
            	for (int i = 0; i < listBanks.length; i++) {
            	    bankIdField.getItems().add(listBanks.get(i).getId());
					bankIdField.getItem[i].setValue(listBanks.get(i).getId());
					bankIdField.getItem[i].setText(listBanks.get(i).getBankName());
				}
			}
		}
	}
	
    @FXML
    public void transactionRegister() {
		String toNameSurname, toIbanAccountNumber = "", description;
        Integer transactionTypeId = 3, userId = 0, accountId = 0, toAccountId = 0, toBankId;
        Double moneyQuantity = 0.0, fieldMoneyQuantity = 0.0, newMoneyQuantity = 0.0;
		LocalDateTime now, transactionDate;
		userId = transactionDAO.getLoginUserId();
		if (userId == 0) {
			transactionDAO.showAlert("Hata", "Kullanıcı oturumunda bir sorun meydana geldi, lütfen sisteme tekrar giriş yapınız.", Alert.AlertType.ERROR);
			transactionDAO.switchToPage(FXMLPath.LOGIN, "Giriş");
		}
		Optional<AccountDTO> accountDTO = accountDAO.findByUserId(userId);
		if (accountDTO.isPresent())
		{
            accountId = accountDTO.getId();
			moneyQuantity = accountDTO.getMoneyQuantity();
			if (accountId == 0) {
				transactionDAO.showAlert("Hata", "Para gönderilecek hesap bulunamadı, lütfen hesap bilgilerinizi güncelleyiniz ya da sisteme tekrar giriş yapınız.", Alert.AlertType.ERROR);
				transactionDAO.switchToPage(FXMLPath.ACCOUNTUPDATE, "Hesap Güncelleme");
			} else {
                toAccountId = Integer.valueOf(toAccountIdField.getText());
				fieldMoneyQuantity = Double.valueOf(moneyQuantityField.getText());
				if (fieldMoneyQuantity == 0.0) {
					transactionDAO.showAlert("Uyarı", "Para miktarı alanını sıfırdan farklı doldurunuz, lütfen işleminizi tekrar deneyiniz.", Alert.AlertType.WARNING);
					transactionDAO.switchToPage(FXMLPath.SENDMONEY, "Para Gönderme");
				}
				newMoneyQuantity = (moneyQuantity - fieldMoneyQuantity);
				toNameSurname = toNameSurnameField.getText().trim();
				toBankId = Integer.valueOf(toBankIdField.getText());
				toIbanAccountNumber = toIbanAccountNumberField.getText().trim();
				description = descriptionField.getText().trim();
				Locale locale = new Locale("tr", "TR");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss", locale);
				LocalDateTime now = LocalDateTime.now().format(formatter);
				transactionDate = now;

				Optional<TransactionDTO> optionalTransactionDTO = Optional.ofNullable(
					(toAccountId == 0) ? TransactionDTO.builder()
                        //.id(0)
                        .transactionTypeId(transactionTypeId)
                        .accountId(accountId)
                        .toAccountId(null)
                        .moneyQuantity(fieldMoneyQuantity)
                        .toNameSurname(toNameSurname)
                        .toBankId(toBankId)
                        .toIbanAccountNumber(toIbanAccountNumber)
						.description(description)
						.transactionDate(transactionDate)
                        .build() :
					TransactionDTO.builder()
                        //.id(0)
                        .transactionTypeId(transactionTypeId)
                        .accountId(accountId)
                        .toAccountId(toAccountId)
                        .moneyQuantity(fieldMoneyQuantity)
                        .toNameSurname(null)
                        .toBankId(null)
                        .toIbanAccountNumber(null)
						.description(description)
						.transactionDate(transactionDate)
                        .build()
					);

				Optional<AccountDTO> accountDTO = accountDAO.findById(accountId);
				if (optionalTransactionDTO.isPresent() && accountDTO.isPresent()) {
					Optional<TransactionDTO> createdTransactionDTO = transactionDAO.create(optionalTransactionDTO.get());
					accountDTO.setMoneyQuantity(newMoneyQuantity);
					Optional<AccountDTO> updatedAccountDTO = accountDAO.update(accountDTO.accountId(), accountDTO);
					if (createdTransactionDTO.isPresent() && updatedAccountDTO.isPresent) {
						transactionDAO.showAlert("Bilgi", "Para gönderildi", Alert.AlertType.INFORMATION);
						transactionDAO.switchToPage(FXMLPath.INDEX, "Anasayfa");
					} else {
						transactionDAO.showAlert("Uyarı", "Para gönderilemedi", Alert.AlertType.ERROR);
						transactionDAO.switchToPage(FXMLPath.SENDMONEY, "Para Gönderme");
					}
				} else {
					transactionDAO.showAlert("Hata", "Para gönderme işleminde bir sorun meydana geldi, lütfen işleminizi tekrar deneyiniz.", Alert.AlertType.ERROR);
					transactionDAO.switchToPage(FXMLPath.SENDMONEY, "Para Gönderme");	
				}
			}
		}
    }
	
    @FXML
    public void switchToPage(String fxmlPath, String title) {
		  accountDAO.switchToPage(fxmlPath, title);
    }
}
