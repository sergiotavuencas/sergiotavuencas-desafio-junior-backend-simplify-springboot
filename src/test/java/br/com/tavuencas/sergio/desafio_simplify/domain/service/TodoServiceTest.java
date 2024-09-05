package br.com.tavuencas.sergio.desafio_simplify.domain.service;

import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.EntityNotFoundException;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorCode;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.InvalidEntityException;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Account;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.AccountRepository;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService service;

    @Mock
    private TodoRepository repository;

    @Mock
    private AccountRepository accountRepository;

    private UUID accountId;
    private Account account;
    private Todo todo;
    private TodoRequestDto request;
    private List<TodoResponseDto> todos;
    private Sort sort;
    private ArgumentCaptor<Todo> todoCaptor;

    @BeforeEach
    void setUp() {
        accountId = UUID.fromString("b7cb411b-c52b-47c1-91f1-fdcbeaf40686");
        account = new Account(accountId, "Sérgio", "sergio@email.com");
        todo = new Todo(1L, "Lavar a louça", "Lavar a louça do café da manhã e da janta", 2, account);
        request = new TodoRequestDto("Lavar a louça", "Lavar a louça do café da manhã e da janta", 2);
        todoCaptor = ArgumentCaptor.forClass(Todo.class);
        TodoResponseDto responseDto = new TodoResponseDto(
                todo.getId(),
                todo.getName(),
                todo.getDescription(),
                todo.isDone(),
                todo.getPriority()
        );
        todos = List.of(
                responseDto,
                responseDto
        );
        sort = Sort.by("priority").descending().and(
                Sort.by("name").ascending()
        );
    }

    @Test
    void whenCreatingTodo_withCorrectData_shouldReturnResponse() {
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(repository.save(todoCaptor.capture())).thenReturn(todo);

        List<TodoResponseDto> response = service.create(accountId, request);

        assertNotNull(response);

        Todo capturedTodo = todoCaptor.getValue();
        assertEquals(request.name(), capturedTodo.getName());
        assertEquals(request.priority(), capturedTodo.getPriority());
        assertEquals(account.getId(), capturedTodo.getAccount().getId());

        verify(repository, times(1)).save(capturedTodo);
    }

    @Test
    void whenCreatingTodo_withNullRequest_shouldThrowException() {
        InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> service.create(accountId, null));

        assertEquals("Dados da tarefa incorretos, verifique os dados inseridos", exception.getMessage());
        assertEquals(ErrorCode.TODO_NOT_VALID, exception.getErrorCode());

        verify(repository, never()).save(todo);
    }

    @Test
    void whenCreatingTodo_withNonExistingAccount_shouldThrowException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.create(accountId, request));

        assertEquals("Conta não encontrada, verifique o ID inserido.", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());

        verify(repository, never()).save(any(Todo.class));
    }

    @Test
    void whenSearchingTodos_withExistingAccount_shouldReturnResponse() {
        when(repository.findTodosByAccountId(accountId, sort)).thenReturn(Optional.of(todos));

        List<TodoResponseDto> response = service.list(accountId);

        assertNotNull(response);
        assertEquals(2, response.size());

        verify(repository, times(1)).findTodosByAccountId(accountId, sort);
    }

    @Test
    void whenFinishingATodo_withExistingEntity_shouldReturnResponse() {
        when(accountRepository.existsById(accountId)).thenReturn(true);

        todo.setDone(true);

        when(repository.save(todoCaptor.capture())).thenReturn(todo);
        when(repository.findById(todo.getId())).thenReturn(Optional.of(todo));
        when(repository.findTodosByAccountId(account.getId(), sort)).thenReturn(Optional.of(todos));

        List<TodoResponseDto> response = service.done(accountId, todo.getId());

        assertNotNull(response);
        assertEquals(2, response.size());

        Todo capturedTodo = todoCaptor.getValue();
        assertEquals(request.name(), capturedTodo.getName());
        assertEquals(request.description(), capturedTodo.getDescription());
        assertEquals(request.priority(), capturedTodo.getPriority());
        assertTrue(capturedTodo.isDone());

        verify(repository, times(1)).save(capturedTodo);
    }

    @Test
    void whenFinishingATodo_withNonExistingAccount_shouldThrowException() {
        when(accountRepository.existsById(accountId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.done(accountId, todo.getId()));

        assertEquals("Conta não encontrada, verifique o ID inserido.", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());

        verify(repository, never()).save(any(Todo.class));
    }


    @Test
    void whenUpdatingTodos_withCorrectData_shouldReturnResponse() {
        when(accountRepository.existsById(accountId)).thenReturn(true);

        todo.setName("Limpar o quarto");
        todo.setDescription("Varrer e passar pano com desinfetante.");

        when(repository.save(todoCaptor.capture())).thenReturn(todo);
        when(repository.findById(todo.getId())).thenReturn(Optional.of(todo));
        when(repository.findTodosByAccountId(account.getId(), sort)).thenReturn(Optional.of(todos));

        TodoRequestDto updatedRequest = new TodoRequestDto("Limpar o quarto", "Varrer e passar pano com desinfetante.", 1);

        List<TodoResponseDto> response = service.update(accountId, todo.getId(), updatedRequest);

        assertNotNull(response);
        assertEquals(2, response.size());

        Todo capturedTodo = todoCaptor.getValue();
        assertEquals("Limpar o quarto", capturedTodo.getName());
        assertEquals("Varrer e passar pano com desinfetante.", capturedTodo.getDescription());
        assertEquals(1, capturedTodo.getPriority());

        verify(repository, times(1)).save(capturedTodo);
    }

    @Test
    void whenUpdatingTodos_withNullRequest_shouldThrowException() {
        InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> service.update(accountId, todo.getId(), null));

        assertEquals("Dados da tarefa incorretos, verifique os dados inseridos", exception.getMessage());
        assertEquals(ErrorCode.TODO_NOT_VALID, exception.getErrorCode());
    }

    @Test
    void whenUpdatingTodos_withNonExistingAccount_shouldThrowException() {
        when(accountRepository.existsById(accountId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.update(accountId, todo.getId(), request));

        assertEquals("Conta não encontrada, verifique o ID inserido.", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());

        verify(repository, never()).save(any(Todo.class));
    }

    @Test
    void whenDeletingATodo_withExistingEntity_shouldReturnResponse() {
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(repository.existsById(todo.getId())).thenReturn(true);
        when(repository.findTodosByAccountId(account.getId(), sort)).thenReturn(Optional.of(todos));

        List<TodoResponseDto> response = service.delete(accountId, todo.getId());

        assertNotNull(response);
//
        verify(repository, times(1)).deleteById(todo.getId());
    }
}
