package api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import model.endpoints.Comments;
import model.entities.Comment;
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

public class CommentsTests {

    private final WebClient webClient = WebClient.create();
    private static String[] ids;
    private static List<Map<String, String>> commentsData;

    @BeforeAll
    static void beforeAll() {

        commentsData = CSVTools.getRecords("src/test/resources/test_data/comments.csv");
        ids = CSVTools.getColumnValues(commentsData, "id");

    }

    private static Stream<String> provideIds() {
        return Arrays.stream(ids);
    }

    private static Stream<Map<String, String>> provideCommentsData() {
        return commentsData.stream();
    }

    @Test
    void testServiceReturnsAllComments() {

        ResponseEntity<String> response = webClient.get()
                .uri(Comments.URL)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode[] comments = JSONTools.getElementsFromJsonArray(response.getBody());
        assertThat(comments).isNotEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceReturnsCommentById(String id) {

        ResponseEntity<String> response = webClient.get()
                .uri(Comments.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void testServiceReturnsNotFoundForNonexistentCommentId() {

        ResponseEntity<String> response = webClient.get()
                .uri(Comments.URL + "/" + 1000000)
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
    void testServiceCreatesComment() {

        ResponseEntity<String> response = webClient.post()
                .uri(Comments.URL)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(Comment.getComment().toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @ParameterizedTest
    @MethodSource("provideCommentsData")
    void testServiceUpdatesComment(Map<String, String> record) {

        Comment comment = new Comment(record);

        ResponseEntity<String> response = webClient.put()
                .uri(Comments.URL + "/" + comment.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(comment.toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        Comment received = new Comment(response.getBody());
        assertThat(received).isEqualTo(comment);

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceDeletesComment(String id) {

        ResponseEntity<String> response = webClient.delete()
                .uri(Comments.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


}
