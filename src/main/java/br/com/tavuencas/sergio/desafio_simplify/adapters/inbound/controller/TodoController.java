package br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.controller;

import br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.exception.ErrorResponse;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoResponseDto;
import br.com.tavuencas.sergio.desafio_simplify.application.dto.TodoRequestDto;
import br.com.tavuencas.sergio.desafio_simplify.domain.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @Operation(summary = "Create a todo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the todo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Not valid todo/Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PostMapping("/{accountId}")
    public ResponseEntity<List<TodoResponseDto>> create(@PathVariable("accountId") UUID accountId, @RequestBody(required = false) TodoRequestDto request) {
        return new ResponseEntity<>(service.create(accountId, request), HttpStatus.CREATED);
    }

    @Operation(summary = "List account todos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Founded account todos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<List<TodoResponseDto>> list(@PathVariable("accountId") UUID accountId) {
        return new ResponseEntity<>(service.list(accountId), HttpStatus.OK);
    }

    @Operation(summary = "Mark todo as done by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marked todo as done",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Account not found/Todo not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PostMapping("/{accountId}/{todoId}/done")
    public ResponseEntity<List<TodoResponseDto>> done(@PathVariable("accountId") UUID accountId, @PathVariable("todoId") Long todoId) {
        return new ResponseEntity<>(service.done(accountId, todoId), HttpStatus.OK);
    }

    @Operation(summary = "Update todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Update the todo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Not valid todo/Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @PutMapping("/{accountId}/{todoId}")
    public ResponseEntity<List<TodoResponseDto>> update(
            @PathVariable("accountId") UUID accountId,
            @PathVariable("todoId") Long todoId, @RequestBody(required = false) TodoRequestDto request) {
        return new ResponseEntity<>(service.update(accountId, todoId, request), HttpStatus.OK);
    }

    @Operation(summary = "Delete todo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Todo deleted"),
            @ApiResponse(
                    responseCode = "400", description = "Not valid todo/Account not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
    })
    @DeleteMapping("/{accountId}/{todoId}")
    public ResponseEntity<List<TodoResponseDto>> delete(@PathVariable("accountId") UUID accountId, @PathVariable("todoId") Long todoId) {
        return new ResponseEntity<>(service.delete(accountId, todoId), HttpStatus.ACCEPTED);
    }
}
