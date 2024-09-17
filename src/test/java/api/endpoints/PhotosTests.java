package api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import model.endpoints.Photos;
import model.entities.Photo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
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

public class PhotosTests {

    private final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurator -> configurator
                            .defaultCodecs()
                            .maxInMemorySize(10 * 1024 * 1024))
                    .build())
            .build();

    private static String[] ids;
    private static List<Map<String, String>> photosData;

    @BeforeAll
    static void beforeAll() {

        photosData = CSVTools.getRecords("src/test/resources/test_data/photos.csv");
        ids = CSVTools.getColumnValues(photosData, "id");

    }

    private static Stream<String> provideIds() {
        return Arrays.stream(ids);
    }

    private static Stream<Map<String, String>> providePhotosData() {
        return photosData.stream();
    }

    @Test
    void testServiceReturnsAllPhotos() {

        ResponseEntity<String> response = webClient.get()
                .uri(Photos.URL)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode[] photos = JSONTools.getElementsFromJsonArray(response.getBody());
        assertThat(photos).isNotEmpty();

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceReturnsPhotoById(String id) {

        ResponseEntity<String> response = webClient.get()
                .uri(Photos.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void testServiceReturnsNotFoundForNonexistentPhotoId() {

        ResponseEntity<String> response = webClient.get()
                .uri(Photos.URL + "/" + 1000000)
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
    void testServiceCreatesPhoto() {

        ResponseEntity<String> response = webClient.post()
                .uri(Photos.URL)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(Photo.getPhoto().toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @ParameterizedTest
    @MethodSource("providePhotosData")
    void testServiceUpdatesPhoto(Map<String, String> record) {

        Photo photo = new Photo(record);

        ResponseEntity<String> response = webClient.patch()
                .uri(Photos.URL + "/" + photo.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(photo.toJSONString()))
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        Photo received = new Photo(response.getBody());
        assertThat(received).isEqualTo(photo);

    }

    @ParameterizedTest
    @MethodSource("provideIds")
    void testServiceDeletesPhoto(String id) {

        ResponseEntity<String> response = webClient.delete()
                .uri(Photos.URL + "/" + id)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
