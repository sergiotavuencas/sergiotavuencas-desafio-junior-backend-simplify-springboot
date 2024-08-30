package br.com.tavuencas.sergio.desafio_simplify;

import br.com.tavuencas.sergio.desafio_simplify.domain.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DesafioSimplifyApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateTodoSuccess() {
        var todo = Todo.builder()
                .name("Lavar a louça")
                .description("Lavar a louça do café da manhã e da janta.")
                .done(false)
                .priority(1)
                .build();

        webTestClient
                .post()
                .uri("/api/todos")
                .bodyValue(todo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo(todo.getName())
                .jsonPath("$[0].description").isEqualTo(todo.getDescription())
                .jsonPath("$[0].done").isEqualTo(todo.isDone())
                .jsonPath("$[0].priority").isEqualTo(todo.getPriority());
    }

    @Test
    void testCreateTodoFailure() {
        webTestClient
                .post()
                .uri("/api/todos")
                .bodyValue(
                        Todo.builder()
                                .name("")
                                .description("")
                                .done(false)
                                .priority(0)
                                .build()
                )
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
