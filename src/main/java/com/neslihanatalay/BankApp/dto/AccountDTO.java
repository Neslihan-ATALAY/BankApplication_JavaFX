package main.java.com.neslihanatalay.BankApp.dto;
import java.time.LocalDateTime;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AccountDTO {
    private Integer id;
    private Integer userId;
    private UserDTO user;
    private String ibanAccountNumber;
    private Double moneyQuantity;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public AccountDTO() {}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getIbanAccountNumber() {
        return ibanAccountNumber;
    }

    public void setIbanAccountNumber(String ibanAccountNumber) {
        this.ibanAccountNumber = ibanAccountNumber;
    }

    public Double getMoneyQuantity() {
        return moneyQuantity;
    }

    public void setMoneyQuantity(Double moneyQuantity) {
        this.moneyQuantity = moneyQuantity;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
