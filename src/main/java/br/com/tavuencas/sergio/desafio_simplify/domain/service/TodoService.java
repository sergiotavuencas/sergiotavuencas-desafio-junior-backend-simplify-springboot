package br.com.tavuencas.sergio.desafio_simplify.domain.service;

import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public List<Todo> create(Todo todo) {
        repository.save(todo);

        return list(); // DRY
    }

    // Pode ser utilizado para aplicar o DRY.
    public List<Todo> list() {
        Sort sortByHighestPriorityOrName = Sort.by("priority").descending().and(
                Sort.by("name").ascending()
        );

        return repository.findAll(sortByHighestPriorityOrName);
    }

    public List<Todo> update(Todo todo) {
        repository.save(todo);

        return list(); // DRY
    }

    public List<Todo> delete(Long id) {
        repository.deleteById(id);

        return list(); // DRY
    }
}
