package br.com.tavuencas.sergio.desafio_simplify.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "todos")
public class Todo implements Serializable {
    private static Long serialVersionLID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;
    private boolean isDone;
    private int priority;

    @ManyToOne
    private Account account;

    public Todo(String name, String description, int priority) {
        this.name = name;
        this.description = description;
        this.isDone = false;
        this.priority = priority;
    }

    public Todo(String name, String description, int priority, Account account) {
        this.name = name;
        this.description = description;
        this.isDone = false;
        this.priority = priority;
        this.account = account;
    }

    public Todo(Long id, String name, String description, int priority, Account account) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDone = false;
        this.priority = priority;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return isDone == todo.isDone && priority == todo.priority && Objects.equals(id, todo.id) && Objects.equals(name, todo.name) && Objects.equals(description, todo.description) && Objects.equals(account, todo.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isDone, priority, account);
    }
}