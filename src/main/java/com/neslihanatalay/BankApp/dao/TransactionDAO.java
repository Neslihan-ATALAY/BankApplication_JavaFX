package com.neslihanatalay.BankApp.dao;

import com.neslihanatalay.BankApp.database.SingletonDBConnection;
import com.neslihanatalay.BankApp.dto.AccountDTO;
import com.neslihanatalay.BankApp.dto.TransactionDTO;
import com.neslihanatalay.BankApp.utils.SceneHelper;
import com.neslihanatalay.BankApp.utils.SpecialColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAO implements IDaoImplements<TransactionDTO> {

    private Connection connection;

    public TransactionDTO() {
        this.connection = SingletonDBConnection.getInstance().getConnection();
    }
	
    @Override
    public Optional<TransactionDTO> create(TransactionDTO TransactionDTO) {
        String sql = "INSERT INTO transactions (transactionTypeId, accountId, toAccountId, moneyQuantity, toNameSurname, toBankId, toIbanAccountNumber, description, dateTime) VALUES(?,?,?,,?,?,?,?,?,?)";
        try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInteger(1, TransactionDTO.getTransactionTypeId());
            preparedStatement.setInteger(2, TransactionDTO.getAccountId());
            preparedStatement.setInteger(3, TransactionDTO.getToAccountId());
            preparedStatement.setDouble(4, TransactionDTO.getMoneyQuantity());
            preparedStatement.setString(5, TransactionDTO.getToNameSurname());
            preparedStatement.setInteger(6, TransactionDTO.getToBankId());
            preparedStatement.setString(7, TransactionDTO.getToIbanAccountNumber());
            preparedStatement.setString(8, TransactionDTO.getDescription());
            preparedStatement.setDateTime(9, TransactionDTO.getTransactionDate());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        TransactionDTO.setId(generatedKeys.getInt(1)); // Otomatik ID set etme
                        return Optional.of(TransactionDTO);
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
    public Optional<List<TransactionDTO>> list() {
        List<TransactionDTO> TransactionDTOList = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
                TransactionDTOList.add(TransactionDTO.builder()
                        .id(resultSet.getInt("id"))
                        .transactionTypeId(resultSet.getInt("transactionTypeId"))
                        .accountId(resultSet.getInt("accountId;"))
                        .toAccountId(resultSet.getInt("toAccountId"))
                        .moneyQuantity(resultSet.getDouble("moneyQuantity"))
                        .toNameSurname(resultSet.getString("toNameSurname"))
                        .toBankId(resultSet.getInt("toBankId"))
                        .toIbanAccountNumber(resultSet.getString("toIbanAccountNumber"))
                        .description(resultSet.getString("description"))
                        .transactionDate(resultSet.getDateTime("transactionDate"))
                        .build());
            }
            return TransactionDTOList.isEmpty() ? Optional.empty() : Optional.of(TransactionDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<AccountDTO>> listByAccountId(Integer accountId) {
        List<ActionDTO> AccountDTOList = new ArrayList<>();
        String sql = "SELECT toAccountId FROM transactions WHERE accountId=?";
		int toAccountId = 0;
        try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInteger(1, accountId);
			ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
				toAccountId = resultSet.getInt("toAccountId");
                String sql2 = "SELECT * FROM accounts WHERE Id=?";
				try {
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
					preparedStatement2.setInteger(1, toAccountId);
					ResultSet resultSet2 = preparedStatement2.executeQuery(sql);

					while (resultSet2.next()) {
					AccountDTOList.add(AccountDTO.builder()
                        .id(resultSet2.getInt("id"))
                        .userId(resultSet2.getInt("userId"))
                        .ibanAccountNumber(resultSet2.getString("ibanAccountNumber"))
                        .moneyQuantity(resultSet2.getDouble("moneyQuantity"))
                        .createDate(resultSet2.getDateTime("createDate"))
                        .updateDate(resultSet2.getDateTime("updateDate"))
                        .build());
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
            }
            return AccountDTOList.isEmpty() ? Optional.empty() : Optional.of(AccountDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<BankDTO>> listBanks() {
        List<BankDTO> BankDTOList = new ArrayList<>();
        String sql = "SELECT * FROM banks";
        try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);

            while (resultSet.next()) {
                BankDTOList.add(BankDTO.builder()
                        .id(resultSet.getInt("id"))
                        .bankName(resultSet.getString("bankName"))
                        .build());
            }
            return BankDTOList.isEmpty() ? Optional.empty() : Optional.of(BankDTOList);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<TransactionDTO> findByIbanAccountNumber(String toIbanAccountNumber) {
        String sql = "SELECT * FROM transactions WHERE toIbanAccountNumber=?";
        return selectSingle(sql, toIbanAccountNumber);
    }

    @Override
    public Optional<TransactionDTO> findById(int id) {
        String sql = "SELECT * FROM transactions WHERE id=?";
        return selectSingle(sql, id);
    }

    @Override
    public Optional<TransactionDTO> update(int id, TransactionDTO TransactionDTO) {
        Optional<TransactionDTO> optionalUpdate = findById(id);
        if (optionalUpdate.isPresent()) {
            String sql = "UPDATE transactions SET transactionTypeId=?, accountId=?, toAccountId=?, moneyQuantity=?, toNameSurname=?, toBankId=?, toIbanAccountNumber=?, description=?, transactionDate=?  WHERE id=?";
            try {
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, TransactionDTO.getTransactionTypeId());
                preparedStatement.setInt(2, TransactionDTO.getAccountId());
                preparedStatement.setInt(3, TransactionDTO.getToAccountId());
                preparedStatement.setDouble(4, TransactionDTO.getMoneyQuantity());
                preparedStatement.setString(5, TransactionDTO.getToNameSurname());
                preparedStatement.setInt(6, TransactionDTO.getToBankId());
                preparedStatement.setString(7, TransactionDTO.getToIbanAccountNumber());
                preparedStatement.setString(8, TransactionDTO.getDescription());
                preparedStatement.setDateTime(9, TransactionDTO.getTransactionDate());
                preparedStatement.setInt(10, id);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    TransactionDTO.setId(id);
                    return Optional.of(TransactionDTO);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<TransactionDTO> delete(int id) {
        Optional<TransactionDTO> optionalDelete = findById(id);
        if (optionalDelete.isPresent()) {
            String sql = "DELETE FROM transactions WHERE id=?";
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
    TransactionDTO mapToObjectDTO(ResultSet resultSet) throws SQLException {
	return TransactionDTO.builder()
        	.id(resultSet.getInt("id"))
        	.transactionTypeId(resultSet.getInt("transactionTypeId"))
      		.accountId(resultSet.getInt("accountId"))
      		.toAccountId(resultSet.getInt("toAccountId"))
        	.moneyQuantity(resultSet.getDouble("moneyQuantity"))
        	.toNameSurname(resultSet.getString("toNameSurname"))
         	.toBankId(resultSet.getInt("toBankId"))
         	.toIbanAccountNumber(resultSet.getString("toIbanAccountNumber"))
         	.description(resultSet.getString("description"))
         	.transactionDate(resultSet.getDateTime("transactionDate"))
         	.build();
    }

    @Override
    public Optional<TransactionDTO> selectSingle(String sql, Object... params) {
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
