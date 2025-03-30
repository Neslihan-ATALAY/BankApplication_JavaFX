package com.neslihanatalay.BankApp.dao;
import com.neslihanatalay.BankApp.database.SingletonDBConnection;
import com.neslihanatalay.BankApp.dto.AccountDTO;
import com.neslihanatalay.BankApp.utils.SceneHelper;
import com.neslihanatalay.BankApp.utils.SpecialColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class AccountDAO implements IDaoImplements<AccountDTO> {
    private Connection connection;

    public AccountDAO() {
        this.connection = SingletonDBConnection.getInstance().getConnection();
    }

    @Override
    public Optional<AccountDTO> create(AccountDTO accountDTO) {
        String sql = "INSERT INTO accounts (userId, ibanAccountNumber, moneyQuantity, createDate, updateDate) VALUES(?,?,?,?,?)";
        try {
			      PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInteger(1, accountDTO.getUserId());
            preparedStatement.setString(2, accountDTO.getIbanAccountNumber());
            preparedStatement.setDouble(3, 0);
            preparedStatement.setDateTime(4, accountDTO.getCreateDate());
            preparedStatement.setDateTime(5, accountDTO.getUpdateDate());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        accountDTO.setId(generatedKeys.getInt(1));
                        return Optional.of(accountDTO);
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
    public Optional<List<AccountDTO>> list() {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try {
	          PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
                accountDTOList.add(accountDTO.builder()
                        .id(resultSet.getInteger("id"))
                        .userId(resultSet.getInteger("userId"))
                        .ibanAccountNumber(resultSet.getString("ibanAccountNumber"))
                        .moneyQuantity(resultSet.getDouble("moneyQuantity"))
						            .createDate(resultSet.getDateTime("createDate"))
						            .updateDate(resultSet.getDateTime("updateDate"))
						            .build());
            }
            return accountDTOList.isEmpty() ? Optional.empty() : Optional.of(accountDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<AccountDTO> findByIbanAccountNumber(String ibanAccountNumber) {
        String sql = "SELECT * FROM accounts WHERE ibanAccountNumber=?";
        return selectSingle(sql, ibanAccountNumber);
    }

    @Override
    public Optional<AccountDTO> findById(int id) {
        String sql = "SELECT * FROM accounts WHERE id=?";
        return selectSingle(sql, id);
    }

    @Override
    public Optional<AccountDTO> findByUserId(Integer userId) {
        String sql = "SELECT * FROM accounts WHERE userId=?";
        return selectSingle(sql, userId);
    }

    @Override
    public Optional<List<AccountDTO>> listByUserId(Integer userId) {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE userId=?";
        try {
	          PreparedStatement preparedStatement = connection.prepareStatement(sql);
	          preparedStatement.setInteger(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
                accountDTOList.add(accountDTO.builder()
                        .id(resultSet.getInteger("id"))
                        .userId(resultSet.getInteger("userId"))
                        .ibanAccountNumber(resultSet.getString("ibanAccountNumber"))
                        .moneyQuantity(resultSet.getDouble("moneyQuantity"))
						            .createDate(resultSet.getDateTime("createDate"))
						            .updateDate(resultSet.getDateTime("updateDate"))
						            .build());
            }
            return accountDTOList.isEmpty() ? Optional.empty() : Optional.of(accountDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<AccountDTO> update(int id, AccountDTO accountDTO) {
        Optional<AccountDTO> optionalUpdate = findById(id);
        if (optionalUpdate.isPresent()) {
            String sql = "UPDATE accounts SET userId=?, ibanAccountNumber=?, moneyQuantity=?, createDate=?, updateDate=?  WHERE id=?";
            try {
				        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInteger(1, accountDTO.getUserId());
                preparedStatement.setString(2, accountDTO.getIbanAccountNumber());
                preparedStatement.setDouble(3, accountDTO.getMoneyQuantity());
                preparedStatement.setDateTime(4, accountDTO.getCreateDate());
                preparedStatement.setDateTime(5, accountDTO.getUpdateDate());
                preparedStatement.setInteger(6, id);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    accountDTO.setId(id);
                    return Optional.of(accountDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<AccountDTO> delete(int id) {
        Optional<AccountDTO> optionalDelete = findById(id);
        if (optionalDelete.isPresent()) {
            String sql = "DELETE FROM accounts WHERE id=?";
            try {
			          PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInteger(1, id);

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
    public accountDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
		  return accountDTO.builder()
        	.id(resultSet.getInteger("id"))
        	.userId(resultSet.getInteger("userId"))
      		.ibanAccountNumber(resultSet.getString("ibanAccountNumber"))
        	.moneyQuantity(resultSet.getDouble("moneyQuantity"))
         	.createDate(resultSet.getDateTime("createDate"))
         	.updateDate(resultSet.getDateTime("updateDate"))
         	.build();
    }

    @Override
    public Optional<AccountDTO> selectSingle(String sql, Object... params) {
        try {
			      PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject((i + 1), params[i]);
            }

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return Optional.of(mapToObjectDTO(resultSet));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}
