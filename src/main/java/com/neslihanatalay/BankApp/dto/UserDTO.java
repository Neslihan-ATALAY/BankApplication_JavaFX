package main.java.com.neslihanatalay.BankApp.dto;
import java.time.LocalDateTime;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String password;
    private String TCNumber;
    private String address;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public UserDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTCNumber() {
        return TCNumber;
    }

    public void setTCNumber(String TCNumber) {
        this.TCNumber = TCNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    /*
    public static void main(String[] args) {
        UserDTO userDTO= UserDTO.builder()
                .id(0)
                .name("name")
				.surname("surname")
                .password("root")
				.TCNumber("11111111111")
				.address("")
                .build();

        System.out.println(userDTO);
    }
    */
}
