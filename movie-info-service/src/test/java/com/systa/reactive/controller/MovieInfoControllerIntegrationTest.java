package com.systa.reactive.controller;

import com.systa.reactive.entity.MovieInfo;
import com.systa.reactive.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerIntegrationTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    private WebTestClient webTestClient;

    private static final String MOVIES_INFO_URL = "/v1/moviesInfo";

    @BeforeEach
    void setUp(){
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearDown(){
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void testCreateMovieInfo(){
        // Given
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        // When
        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var expectedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert expectedMovieInfo != null;
                    assert expectedMovieInfo.getMovieInfoId() != null;
                });

        // Then
    }

    @Test
    void testGetAllMovies(){
        // Given

        // When
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);

        // Then
    }

    @Test
    void testGetMovieById(){

        // Given
        var movieInfoId = "abc";

        // When
//        webTestClient
//                .get()
//                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
//                .exchange()
//                .expectStatus()
//                .is2xxSuccessful()
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(movieInfo);
//                });

        webTestClient
                .get()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");

        // Then

    }

    @Test
    void testGetMovieByIdNotFound(){

        // Given
        var movieInfoId = "dsdfd";

        // When
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNotFound();

        // Then

    }

    @Test
    void testUpdateMovieInfo(){
        // Given
        var movieInfo = new MovieInfo(null, "Dark Knight Rises1",
                2014, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));
        var movieInfoId = "abc";

        // When
        webTestClient
                .put()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var expectedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert expectedMovieInfo != null;
                    assert expectedMovieInfo.getMovieInfoId() != null;
                    assert expectedMovieInfo.getName().equals("Dark Knight Rises1");
                    assert expectedMovieInfo.getYear() == 2014;
                });

        // Then
    }

    @Test
    void testUpdateMovieInfoNotFound(){
        // Given
        var movieInfo = new MovieInfo(null, "Dark Knight Rises1",
                2014, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));
        var movieInfoId = "degdf";

        // When
        webTestClient
                .put()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isNotFound();

        // Then
    }

    @Test
    void testDeleteMovieById(){
        // Given
        var movieInfoId = "abc";

        // When
        webTestClient
                .delete()
                .uri(MOVIES_INFO_URL+"/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNoContent();

        // Then
    }

}