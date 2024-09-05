package br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    ACCOUNT_NOT_FOUND("Conta não encontrada, verifique o ID inserido."),
    TODO_NOT_FOUND("Tarefa não encontrada, verifique o ID inserido."),
    ACCOUNT_NOT_VALID("Dados de conta incorretos, verifique os dados inseridos"),
    TODO_NOT_VALID("Dados da tarefa incorretos, verifique os dados inseridos");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
