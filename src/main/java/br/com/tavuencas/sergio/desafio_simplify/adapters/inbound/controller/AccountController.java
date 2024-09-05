package br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.controller;

import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorResponse;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.AccountResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v2/accounts")
@Slf4j
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @Operation(summary = "Create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the account",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Not valid account",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody(required = false) @Valid AccountRequestDto account) {
        return new ResponseEntity<>(service.create(account), HttpStatus.CREATED);
    }

    @Operation(summary = "Find account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Found the account",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> find(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(service.find(id), HttpStatus.OK);
    }

    @Operation(summary = "Update account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Updated the account",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Not valid account/Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> update(@PathVariable("id") UUID id, @RequestBody(required = false) AccountRequestDto account) {
        return new ResponseEntity<>(service.update(id, account), HttpStatus.OK);
    }

    @Operation(summary = "Delete account by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Account deleted"),
            @ApiResponse(
                    responseCode = "400", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        return service.delete(id);
    }
}
