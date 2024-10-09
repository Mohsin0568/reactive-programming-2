package com.systa.reactive.controller;

import com.systa.reactive.entity.MovieInfo;
import com.systa.reactive.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
public class MovieInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MovieInfoService movieInfoService;

    private static final String MOVIES_INFO_URL = "/v1/moviesInfo";

    @Test
    void testGetAllMovies(){
        // Given
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        when(movieInfoService.getAllMovies()).thenReturn(Flux.fromIterable(movieInfos));

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
        var movieInfos = new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(movieInfoService.getMovieById(movieInfoId)).thenReturn(Mono.just(movieInfos));

        // When
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
    void testCreateMovieInfo(){
        // Given
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        var exptectedMovieInfo = new MovieInfo("mockId", "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(movieInfoService.createMovieInfo(movieInfo)).thenReturn(Mono.just(exptectedMovieInfo));

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
                    var actualMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert actualMovieInfo != null;
                    assert actualMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId", actualMovieInfo.getMovieInfoId());
                });

        // Then
    }

    @Test
    void testCreateMovieInfoValidation(){
        // Given
        var movieInfo = new MovieInfo(null, "",
                -2005, List.of(), LocalDate.parse("2005-06-15"));

        // When
        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var actualMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(actualMovieInfo);
                    assertTrue(actualMovieInfo.contains("movieInfo.name should not be null"));
                    assertTrue(actualMovieInfo.contains("movieInfo.year should be a positive value"));
                });

        // Then
    }

    @Test
    void testUpdateMovieInfo(){
        // Given
        var movieInfo = new MovieInfo(null, "Dark Knight Rises1",
                2014, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));
        var expectedMovieInfo = new MovieInfo("abc", "Dark Knight Rises1",
                2014, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        var movieInfoId = "abc";
        when(movieInfoService.updateMovieInfo(movieInfo, movieInfoId)).thenReturn(Mono.just(expectedMovieInfo));

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
                    var actualMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert actualMovieInfo != null;
                    assert actualMovieInfo.getMovieInfoId() != null;
                    assert actualMovieInfo.getName().equals("Dark Knight Rises1");
                    assert actualMovieInfo.getYear() == 2014;
                });

        // Then
    }

    @Test
    void testDeleteMovieById(){
        // Given
        var movieInfoId = "abc";
        when(movieInfoService.deleteMovieById(movieInfoId)).thenReturn(Mono.empty());

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
