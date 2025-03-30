package com.neslihanatalay.BankApp.dao;

import java.util.List;
import java.util.Optional;

public interface ICrud<T> {

    // CREATE
    Optional<T> create(T t);

    // LIST
    Optional<List<T>> list();
    Optional<List<T>> listByUserId(T t);
    Optional<List<T>> listByAccountId(T t);
    Optional<List<T>> listBanks();

    // FIND
    Optional<T> findByTCNumber(T t);
    Optional<T> findByIbanAccountNumber(T t);
    Optional<T> findByUserId(T t);
    Optional<T> findById(T t);

    // UPDATE
    Optional<T> update(int id, T t);

    // DELETE
    Optional<T> delete(int id);
}
