package api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import model.endpoints.ToDos;
import model.entities.Post;
import model.entities.ToDo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import utils.CSVTools;
import utils.JSONTools;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ToDosTests {

    private final WebClient webClient = WebClient.create();
    private static String[] ids;
    private static List<Map<String, String>> todosData;

    @BeforeAll
    static void beforeAll() {

        todosData = CSVTools.getRecords("src/test/resources/test_data/todos.csv");
        ids = CSVTools.getColumnValues(todosData, "id");

    }

    private static Stream<String> provideIds() {
        return Arrays.stream(ids);
    }

    private static Stream<Map<String, String>> provideTodosData() {
        return todosData.stream();
    }

    @Test
    void testServiceReturnsAllTodos() {

        ResponseEntity<String> response = webClient.get()
                .uri(ToDos.URL)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode[] todos = JSONTools.getElementsFromJsonArray(response.getBody());
        assertThat(todos).isNotEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceReturnsTodoById(String id) {

        ResponseEntity<String> response = webClient.get()
                .uri(ToDos.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void testServiceReturnsNotFoundForNonexistentTodoId() {

        ResponseEntity<String> response = webClient.get()
                .uri(ToDos.URL + "/" + 1000000)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse.statusCode() == HttpStatus.NOT_FOUND
                        ? Mono.empty()
                        : Mono.error(new RuntimeException("Unexpected status code")))
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void testServiceCreatesTodo() {

        ResponseEntity<String> response = webClient.post()
                .uri(ToDos.URL)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(Post.getPost().toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @ParameterizedTest
    @MethodSource("provideTodosData")
    void testServiceUpdatesTodo(Map<String, String> record) {

        ToDo toDo = new ToDo(record);

        ResponseEntity<String> response = webClient.patch()
                .uri(ToDos.URL + "/" + toDo.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toDo.toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        ToDo received = new ToDo(response.getBody());
        assertThat(received).isEqualTo(toDo);

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceDeletesTodo(String id) {

        ResponseEntity<String> response = webClient.delete()
                .uri(ToDos.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


}
