package br.com.tavuencas.sergio.desafio_simplify.domain.repository;

import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, Long> {

//    @Query("SELECT id, name, description, is_done, priority FROM Todos t WHERE t.account_id =: accountId")
    Optional<List<TodoResponseDto>> findTodosByAccountId(@Param("accountId") UUID accountId, Sort sort);
}
