package br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ACCOUNT_NOT_FOUND(1000),
    TODO_NOT_FOUND(1001),
    ACCOUNT_NOT_VALID(2000),
    TODO_NOT_VALID(2001),
    ACCOUNT_ID_IS_NULL(3000),
    TODO_ID_IS_NULL(3001);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }
}
