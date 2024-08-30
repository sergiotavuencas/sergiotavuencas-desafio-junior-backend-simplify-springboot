package br.com.tavuencas.sergio.desafio_simplify.domain.repository;

import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
