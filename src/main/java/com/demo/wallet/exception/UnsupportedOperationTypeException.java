package com.demo.wallet.exception;

public class UnsupportedOperationTypeException extends RuntimeException {

    public UnsupportedOperationTypeException() {
        super("Quantidade informada do ativo é maior que a quantidade disponível");
    }
}
