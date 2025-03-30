package main.java.com.neslihanatalay.BankApp.dao;

import main.java.com.neslihanatalay.BankApp.database.SingletonDBConnection;
import main.java.com.neslihanatalay.BankApp.dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginDAO implements IDaoImplements<UserDTO> {

    private Connection connection;

    public LoginDAO() {
        this.connection = SingletonDBConnection.getInstance().getConnection();
    }

    @Override
    public Optional<UserDTO> findByTCNumber(String TCNumber) {
        String sql = "SELECT * FROM users WHERE TCNumber=?";
        return selectSingle(sql, TCNumber);
    }

    @Override
    public Optional<UserDTO> findById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        return selectSingle(sql,id);
    }

    @Override
    UserDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
	    return UserDTO.builder()
        	.id(resultSet.getInt("id"))
        	.name(resultSet.getString("name"))
      		.surname(resultSet.getString("surname"))
      		.email(resultSet.getString("email"))
        	.password(resultSet.getString("password"))
        	.TCNumber(resultSet.getString("TCNumber"))
        	.telephoneNumber(resultSet.getString("telephoneNumber"))
         	.city(resultSet.getString("city"))
         	.address(resultSet.getString("address"))
          .createDate(resultSet.getDateTime("createDate"))
          .updateDate(resultSet.getDateTime("updateDate"))
         	.build();
    }
	
    @Override
    public Optional<UserDTO> selectSingle(String sql, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject((i + 1), params[i]);
            }
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return Optional.of(mapToObjectDTO(resultSet));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional loginUser(String TCNumber, String password) {
        String sql = "SELECT * FROM users WHERE TCNumber=? AND password=?";
        return selectSingle(sql, TCNumber, password);
    }
}
