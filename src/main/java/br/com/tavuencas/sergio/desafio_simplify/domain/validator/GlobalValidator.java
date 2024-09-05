package br.com.tavuencas.sergio.desafio_simplify.domain.validator;

import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GlobalValidator {

    private static final String NAME_IS_BLANK = "Por favor insira um nome.";
    private static final String PRIORITY_IS_BLANK = "Por favor insira a prioridade com valor entre 1 e 5.";

    private GlobalValidator() {
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static List<String> validateAccount(AccountRequestDto request) {
        if (request == null) {
            return List.of(
                    NAME_IS_BLANK,
                    "Por favor insira um e-mail."
            );
        }

        List<String> errors = new ArrayList<>();

        if (request.name() == null || request.name().isBlank()) {
            errors.add(NAME_IS_BLANK);
        }
        if (request.email() == null || request.email().isBlank()) {
            errors.add("Por favor insira um e-mail.");
        }

        return errors;
    }

    public static List<String> validateTodo(TodoRequestDto request) {
        if (request == null) {
            return List.of(
                    NAME_IS_BLANK,
                    PRIORITY_IS_BLANK
            );
        }

        List<String> errors = new ArrayList<>();

        if (request.name() == null || request.name().isBlank()) {
            errors.add(NAME_IS_BLANK);
        }
        if (request.priority() <= 0 || request.priority() > 5) {
            errors.add(PRIORITY_IS_BLANK);
        }

        return errors;
    }
}
