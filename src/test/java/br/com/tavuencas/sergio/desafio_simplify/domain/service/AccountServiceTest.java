package br.com.tavuencas.sergio.desafio_simplify.domain.service;

import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.EntityNotFoundException;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorCode;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.InvalidEntityException;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Account;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository repository;

    private UUID id;
    private Account account;
    private AccountRequestDto request;
    private ArgumentCaptor<Account> accountCaptor;

    @BeforeEach
    void setUp() {
        id = UUID.fromString("b7cb411b-c52b-47c1-91f1-fdcbeaf40686");
        account = new Account(id, "Sérgio", "sergio@email.com");
        request = new AccountRequestDto(account.getName(), account.getEmail());
        accountCaptor = ArgumentCaptor.forClass(Account.class);
    }

    @Test
    void whenCreatingAccount_withCorrectData_shouldReturnResponse() {
        when(repository.save(accountCaptor.capture())).thenReturn(account);

        AccountResponseDto response = service.create(request);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
        assertEquals(request.email(), response.email());

        Account capturedAccount = accountCaptor.getValue();
        assertEquals("Sérgio", capturedAccount.getName());
        assertEquals("sergio@email.com", capturedAccount.getEmail());

        verify(repository, times(1)).save(capturedAccount);
    }

    @Test
    void whenCreatingAccount_withNullRequest_shouldThrowException() {
        InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> service.create(null));

        assertEquals("Dados de conta incorretos, verifique os dados inseridos", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_VALID, exception.getErrorCode());

        verify(repository, never()).save(account);
    }

    @Test
    void whenSearchingAccount_withExistingId_shouldReturnResponse() {
        when(repository.findById(id)).thenReturn(Optional.of(account));

        AccountResponseDto response = service.find(id);

        assertNotNull(response);
        assertEquals(account.getName(), response.name());
        assertEquals(account.getEmail(), response.email());

        verify(repository, times(1)).findById(id);
    }

    @Test
    void whenSearchingAccount_withNonExistingId_shouldThrowException() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.find(id));

        assertEquals("Conta não encontrada, verifique o ID inserido.", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());

        verify(repository, times(1)).findById(id);
    }

    @Test
    void whenUpdatingAccount_withCorrectData_shouldReturnResponse() {
        account.setName("Sérgio Vicente");
        account.setEmail("sergio_vicente@email.com");

        when(repository.save(accountCaptor.capture())).thenReturn(account);
        when(repository.findById(id)).thenReturn(Optional.of(account));

        AccountRequestDto updatedRequest = new AccountRequestDto("Sérgio Vicente", "sergio_vicente@email.com");

        AccountResponseDto updatedResponse = service.update(id, updatedRequest);

        assertNotNull(updatedResponse);
        assertEquals(account.getName(), updatedResponse.name());
        assertEquals(account.getEmail(), updatedResponse.email());

        Account capturedAccount = accountCaptor.getValue();
        assertEquals("Sérgio Vicente", capturedAccount.getName());
        assertEquals("sergio_vicente@email.com", capturedAccount.getEmail());

        verify(repository, times(1)).save(capturedAccount);
    }

    @Test
    void whenUpdatingAccount_withNullRequest_shouldThrowException() {
        InvalidEntityException exception = assertThrows(InvalidEntityException.class, () -> service.update(id, null));

        assertEquals("Dados de conta incorretos, verifique os dados inseridos", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_VALID, exception.getErrorCode());

        verify(repository, never()).save(account);
    }

    @Test
    void whenDeletingAccount_withExistingId_shouldReturnResponse() {
        when(repository.existsById(id)).thenReturn(true);

        ResponseEntity<Void> response = service.delete(id);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void whenDeletingAccount_withNonExistingId_shouldThrowException() {
        when(repository.existsById(id)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.delete(id));

        assertEquals("Conta não encontrada, verifique o ID inserido.", exception.getMessage());
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, exception.getErrorCode());

        verify(repository, times(0)).deleteById(id);
    }
}
