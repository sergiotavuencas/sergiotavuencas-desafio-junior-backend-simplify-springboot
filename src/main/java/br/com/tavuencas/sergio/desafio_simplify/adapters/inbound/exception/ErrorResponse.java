package br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception;

import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        ErrorCode errorCode,
        List<String> errors
) {
}
