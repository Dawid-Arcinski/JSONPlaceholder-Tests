package api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import model.endpoints.Albums;
import model.entities.Album;
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

public class AlbumsTests {

    private final WebClient webClient = WebClient.create();
    private static String[] ids;
    private static List<Map<String, String>> albumsData;

    @BeforeAll
    static void beforeAll() {

        albumsData = CSVTools.getRecords("src/test/resources/test_data/albums.csv");
        ids = CSVTools.getColumnValues(albumsData, "id");

    }

    private static Stream<String> provideIds() {
        return Arrays.stream(ids);
    }

    private static Stream<Map<String, String>> provideAlbumsData() {
        return albumsData.stream();
    }

    @Test
    void testServiceReturnsAllAlbums() {

        ResponseEntity<String> response = webClient.get()
                .uri(Albums.URL)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode[] albums = JSONTools.getElementsFromJsonArray(response.getBody());
        assertThat(albums).isNotEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceReturnsAlbumById(String id) {

        ResponseEntity<String> response = webClient.get()
                .uri(Albums.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void testServiceReturnsNotFoundForNonexistentAlbumId() {

        ResponseEntity<String> response = webClient.get()
                .uri(Albums.URL + "/" + 1000000)
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
    void testServiceCreatesAlbum() {

        ResponseEntity<String> response = webClient.post()
                .uri(Albums.URL)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(Album.getAlbum().toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @ParameterizedTest
    @MethodSource("provideAlbumsData")
    void testServiceUpdatesAlbum(Map<String, String> record) {

        Album album = new Album(record);

        ResponseEntity<String> response = webClient.patch()
                .uri(Albums.URL + "/" + album.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(album.toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        Album received = new Album(response.getBody());
        assertThat(received).isEqualTo(album);

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceDeletesAlbum(String id) {

        ResponseEntity<String> response = webClient.delete()
                .uri(Albums.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
