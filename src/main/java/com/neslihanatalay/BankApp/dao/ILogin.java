package com.neslihanatalay.BankApp.dao;

import java.util.Optional;

public abstract ILogin <T> {

    private static Integer loginUserId;

    public static Integer getLoginUserId() { return loginUserId; }
    public static void setLoginUserId(Integer loginUserId) { this.loginUserId = loginUserId; }

    Optional<T> loginUser(String username, String password);
}
