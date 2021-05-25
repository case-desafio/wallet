package com.demo.wallet.exception;

public class UserAccountAlreadyRegisteredException extends RuntimeException {

    public UserAccountAlreadyRegisteredException() {
        super("Conta de usuário já cadastrada");
    }
}
