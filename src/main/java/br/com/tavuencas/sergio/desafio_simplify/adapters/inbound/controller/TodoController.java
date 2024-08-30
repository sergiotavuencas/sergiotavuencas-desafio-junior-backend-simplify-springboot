package br.com.tavuencas.sergio.desafio_simplify.adapters.inbound.controller;

import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import br.com.tavuencas.sergio.desafio_simplify.domain.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<Todo>> create(@RequestBody @Valid Todo todo) {
        return new ResponseEntity<>(service.create(todo), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> list() {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<Todo>> update(@RequestBody Todo todo) {
        return new ResponseEntity<>(service.update(todo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Todo>> delete(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.delete(id), HttpStatus.ACCEPTED);
    }
}
