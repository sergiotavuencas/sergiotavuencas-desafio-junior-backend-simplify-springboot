package br.com.tavuencas.sergio.desafio_simplify.application.dto;

import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String name,
        String email
) {
}
