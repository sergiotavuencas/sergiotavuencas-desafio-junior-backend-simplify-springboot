package br.com.tavuencas.sergio.desafio_simplify.application.dto;

public record TodoResponseDto(
        Long id,
        String name,
        String description,
        boolean isDone,
        int priority
) {
}
