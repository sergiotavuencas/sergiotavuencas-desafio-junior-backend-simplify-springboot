package br.com.tavuencas.sergio.desafio_simplify.domain.service;

import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.EntityNotFoundException;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorCode;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorMessage;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.InvalidEntityException;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Account;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.AccountRepository;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.TodoRepository;
import br.com.tavuencas.sergio.desafio_simplify.domain.validator.GlobalValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    private final AccountRepository accountRepository;

    private final Sort sortByHighestPriorityOrName = Sort.by("priority").descending().and(
            Sort.by("name").ascending()
    );

    public List<TodoResponseDto> create(UUID accountId, TodoRequestDto request) {
        List<String> errors = GlobalValidator.validateTodo(request);

        if (!errors.isEmpty()) {
            log.error("TodoService - create() - InvalidEntityException - Not valid entity {}", request);
            throw new InvalidEntityException(ErrorMessage.TODO_NOT_VALID.getMessage(), ErrorCode.TODO_NOT_VALID, errors);
        }

        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent()) {
            var todo = Todo.builder()
                    .name(request.name())
                    .description(request.description())
                    .isDone(false)
                    .priority(request.priority())
                    .account(account.get())
                    .build();

            repository.save(todo);

            return list(accountId);
        }

        log.error("TodoService - create() - EntityNotFoundException - Entity with ID {} not found", accountId);
        throw new EntityNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public List<TodoResponseDto> list(UUID accountId) {
        Optional<List<TodoResponseDto>> todos = repository.findTodosByAccountId(accountId, sortByHighestPriorityOrName);

        return todos.orElseGet(List::of);
    }

    public List<TodoResponseDto> done(UUID accountId, Long todoId) {
        if (accountRepository.existsById(accountId)) {
            Todo todo = findEntity(todoId);

            todo.setDone(true);
            repository.save(todo);

            return list(accountId);
        }

        log.error("TodoService - done() - EntityNotFoundException - Entity with ID {} not found", accountId);
        throw new EntityNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public List<TodoResponseDto> update(UUID accountId, Long todoId, TodoRequestDto request) {
        List<String> errors = GlobalValidator.validateTodo(request);

        if (!errors.isEmpty()) {
            log.error("TodoService - update() - InvalidEntityException - Not valid entity {}", request);
            throw new InvalidEntityException(ErrorMessage.TODO_NOT_VALID.getMessage(), ErrorCode.TODO_NOT_VALID, errors);
        }

        if (accountRepository.existsById(accountId)) {
            Todo todo = findEntity(todoId);

            todo.setName(request.name());
            todo.setDescription(request.description());
            todo.setPriority(request.priority());

            repository.save(todo);

            return list(accountId);
        }

        log.error("TodoService - update() - EntityNotFoundException - Entity with ID {} not found", accountId);
        throw new EntityNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public List<TodoResponseDto> delete(UUID accountId, Long todoId) {
        if (accountRepository.existsById(accountId)) {
            if (repository.existsById(todoId)) {
                repository.deleteById(todoId);
                return list(accountId);
            }

            log.error("TodoService - delete() - EntityNotFoundException - Entity with ID {} not found", todoId);
            throw new EntityNotFoundException(ErrorMessage.TODO_NOT_FOUND.getMessage(), ErrorCode.TODO_NOT_FOUND);
        }

        log.error("TodoService - delete() - EntityNotFoundException - Entity with ID {} not found", accountId);
        throw new EntityNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    private Todo findEntity(Long todoId) {
        Optional<Todo> todo = repository.findById(todoId);

        if (todo.isPresent()) {
            return todo.get();
        }

        log.error("TodoService - findEntity() - EntityNotFoundException - Entity with ID {} not found", todoId);
        throw new EntityNotFoundException(ErrorMessage.TODO_NOT_FOUND.getMessage(), ErrorCode.TODO_NOT_FOUND);
    }
}
