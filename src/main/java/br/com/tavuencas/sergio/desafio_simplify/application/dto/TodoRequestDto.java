package br.com.tavuencas.sergio.desafio_simplify.application.dto;

public record TodoRequestDto(
        String name,
        String description,
        int priority
) {
}
