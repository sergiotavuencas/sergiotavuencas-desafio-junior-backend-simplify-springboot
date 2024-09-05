package br.com.tavuencas.sergio.desafio_simplify.domain.service;

import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.EntityNotFoundException;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorCode;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorMessage;
import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.InvalidEntityException;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.model.Account;
import br.com.tavuencas.sergio.desafio_simplify.domain.repository.AccountRepository;
import br.com.tavuencas.sergio.desafio_simplify.domain.validator.GlobalValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public AccountResponseDto create(AccountRequestDto request) {
        List<String> errors = GlobalValidator.validateAccount(request);

        if (!errors.isEmpty()) {
            log.error("AccountService - create() - InvalidEntityException - Not valid entity {}", errors);
            throw new InvalidEntityException(ErrorMessage.ACCOUNT_NOT_VALID.getMessage(), ErrorCode.ACCOUNT_NOT_VALID, errors);
        }

        Account account = new Account();

        BeanUtils.copyProperties(request, account);
        repository.save(account);

        return new AccountResponseDto(
                account.getId(),
                account.getName(),
                account.getEmail());
    }

    public AccountResponseDto find(UUID id) {
        Account account = findEntity(id);

        return new AccountResponseDto(
                account.getId(),
                account.getName(),
                account.getEmail()
        );
    }

    public AccountResponseDto update(UUID id, AccountRequestDto request) {
        List<String> errors = GlobalValidator.validateAccount(request);

        if (!errors.isEmpty()) {
            log.error("AccountService - update() - InvalidEntityException - Not valid entity {}", errors);
            throw new InvalidEntityException(ErrorMessage.ACCOUNT_NOT_VALID.getMessage(), ErrorCode.ACCOUNT_NOT_VALID, errors);
        }

        Account account = findEntity(id);

        BeanUtils.copyProperties(request, account);
        repository.save(account);

        return new AccountResponseDto(
                account.getId(),
                account.getName(),
                account.getEmail());
    }

    public ResponseEntity<Void> delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        log.error("AccountService - delete() - Entity with ID {} not found", id);
        throw new EntityNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
    }

    private Account findEntity(UUID id) {
        Optional<Account> account = repository.findById(id);

        if (account.isPresent()) {
            return account.get();
        }

        log.error("AccountService - findEntity() - Entity with ID {} not found", id);
        throw new EntityNotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND.getMessage(), ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
