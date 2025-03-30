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

public class UserDAO implements IDaoImplements<UserDTO> {

    private Connection connection;

    public UserDAO() {
        this.connection = SingletonDBConnection.getInstance().getConnection();
    }

    @Override
    public Optional<UserDTO> create(UserDTO userDTO) {
        String sql = "INSERT INTO users (name, surname, password, TCNumber, address, createDate, updateDate) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userDTO.getName());
            preparedStatement.setString(2, userDTO.getSurname());
            preparedStatement.setString(4, userDTO.getPassword());
            preparedStatement.setString(5, userDTO.getTCNumber());
            preparedStatement.setString(8, userDTO.getAddress());
            preparedStatement.setDateTime(9, userDTO.getCreateDate());
            preparedStatement.setDateTime(10, userDTO.getUpdateTime());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        userDTO.setId(generatedKeys.getInt(1));
                        return Optional.of(userDTO);
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<UserDTO>> list() {
        List<UserDTO> userDTOList = new ArrayList<UserDTO>();
        String sql = "SELECT * FROM users";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
                userDTOList.add(UserDTO.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .surname(resultSet.getString("surname"))
                        .password(resultSet.getString("password"))
                        .TCNumber(resultSet.getString("TCNumber"))
                        .address(resultSet.getString("address"))
                        .createDate(resultSet.getDateTime("createDate"))
                        .updateDate(resultSet.getDateTime("updateDate"))
			.build());
            }
            return Optional.of(userDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
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
    public Optional<UserDTO> update(int id, UserDTO userDTO) {
        Optional<UserDTO> optionalUpdate = findById(id);
        if (optionalUpdate.isPresent()) {
            String sql = "UPDATE users SET name=?, surname=?, password=?, TCNumber=?, address=?, createDate=?, updateDate=?  WHERE id=?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userDTO.getName());
                preparedStatement.setString(2, userDTO.getSurname());
                preparedStatement.setString(3, userDTO.getPassword());
                preparedStatement.setString(4, userDTO.getTCNumber());
                preparedStatement.setString(5, userDTO.getAddress());
            	preparedStatement.setDateTime(6, userDTO.getCreateDate());
            	preparedStatement.setDateTime(7, userDTO.getUpdateTime());
                preparedStatement.setInt(8, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    userDTO.setId(id);
                    return Optional.of(userDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> delete(int id) {
        Optional<UserDTO> optionalDelete = findById(id);
        if (optionalDelete.isPresent()) {
            String sql = "DELETE FROM users WHERE id=?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    return optionalDelete;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    UserDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
	    return UserDTO.builder()
        	.id(resultSet.getInt("id"))
        	.name(resultSet.getString("name"))
      		.surname(resultSet.getString("surname"))
        	.password(resultSet.getString("password"))
        	.TCNumber(resultSet.getString("TCNumber"))
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
}
