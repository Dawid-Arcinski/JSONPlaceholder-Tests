package api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import model.endpoints.Posts;
import model.entities.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import utils.JSONTools;
import utils.CSVTools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.web.reactive.function.client.WebClient;

public class PostsTests {

    private final WebClient webClient = WebClient.create();
    private static String[] ids;
    private static List<Map<String, String>> postsData;

    @BeforeAll
    static void beforeAll() {

        postsData = CSVTools.getRecords("src/test/resources/test_data/posts.csv");
        ids = CSVTools.getColumnValues(postsData, "id");

    }

    private static Stream<String> provideIds() {
        return Arrays.stream(ids);
    }

    private static Stream<Map<String, String>> providePostData() {
        return postsData.stream();
    }

    @Test
//    @Description("Test that verifies the service returns all posts correctly.")
    void testServiceReturnsAllPosts() {

        ResponseEntity<String> response = webClient.get()
                .uri(Posts.URL)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode[] posts = JSONTools.getElementsFromJsonArray(response.getBody());
        assertThat(posts).isNotEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceReturnsPostById(String id) {

        ResponseEntity<String> response = webClient.get()
                .uri(Posts.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void testServiceReturnsNotFoundForNonexistentPostId() {

        ResponseEntity<String> response = webClient.get()
                .uri(Posts.URL + "/" + 1000000)
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
    void testServiceCreatesPost() {

        ResponseEntity<String> response = webClient.post()
                .uri(Posts.URL)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(Post.getPost().toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @ParameterizedTest
    @MethodSource("providePostData")
    void testServiceUpdatesPost(Map<String, String> record) {

        Post post = new Post(record);

        ResponseEntity<String> response = webClient.patch()
                .uri(Posts.URL + "/" + post.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(post.toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        Post received = new Post(response.getBody());
        assertThat(received).isEqualTo(post);

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceDeletesPost(String id) {

        ResponseEntity<String> response = webClient.delete()
                .uri(Posts.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
