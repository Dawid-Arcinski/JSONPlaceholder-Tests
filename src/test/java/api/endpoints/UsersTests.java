package api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import model.endpoints.Users;
import model.entities.User;
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

public class UsersTests {

    private final WebClient webClient = WebClient.create();
    private static String[] ids;
    private static List<Map<String, String>> userData;

    @BeforeAll
    static void beforeAll() {

        userData = CSVTools.getRecords("src/test/resources/test_data/users.csv");
        ids = CSVTools.getColumnValues(userData, "id");

    }

    private static Stream<String> provideIds() {
        return Arrays.stream(ids);
    }

    private static Stream<Map<String, String>> provideUserData() {
        return userData.stream();
    }

    @Test
    void testServiceReturnsAllUsers() {

        ResponseEntity<String> response = webClient.get()
                .uri(Users.URL)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode[] users = JSONTools.getElementsFromJsonArray(response.getBody());
        assertThat(users).isNotEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceReturnsUserById(String id) {

        ResponseEntity<String> response = webClient.get()
                .uri(Users.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void testServiceReturnsNotFoundForNonexistentUserId() {

        ResponseEntity<String> response = webClient.get()
                .uri(Users.URL + "/" + 1000000)
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
    void testServiceCreatesUser() {

        ResponseEntity<String> response = webClient.post()
                .uri(Users.URL)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(User.getUser().toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @ParameterizedTest
    @MethodSource("provideUserData")
    void testServiceUpdatesUser(Map<String, String> record) {

        User user = new User(record);

        ResponseEntity<String> response = webClient.put()
                .uri(Users.URL + "/" + user.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(user.toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        User received = new User(response.getBody());
        assertThat(received).isEqualTo(user);

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceDeletesUser(String id) {

        ResponseEntity<String> response = webClient.delete()
                .uri(Users.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
