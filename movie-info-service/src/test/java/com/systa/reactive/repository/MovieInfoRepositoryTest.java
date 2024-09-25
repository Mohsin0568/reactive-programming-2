package com.systa.reactive.repository;

import com.systa.reactive.entity.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

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
    void findAll(){

        // Given

        // When
        var moviesFlux = movieInfoRepository.findAll();

        // Then
        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void findById(){
        // Given

        // When
        var movieMono = movieInfoRepository.findById("abc");

        // Then
        StepVerifier.create(movieMono)
//                .expectNextCount(1)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo(){
        // Given
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        // When
        var movieMono = movieInfoRepository.save(movieInfo);

        // Then
        StepVerifier.create(movieMono)
                .assertNext(movieInfo1 -> {
                    assertNotNull(movieInfo1.getMovieInfoId());
                    assertEquals("Batman Begins1", movieInfo1.getName());
                });
    }

    @Test
    void updateMovieInfo() {
        // Given
        var movie = movieInfoRepository.findById("abc").block();
        movie.setYear(2021);

        // When
        var monoMovie = movieInfoRepository.save(movie);

        // Then
        StepVerifier.create(monoMovie)
                .assertNext(movieInfo -> {
                    assertEquals(2021, movieInfo.getYear());
                });
    }

    @Test
    void deleteMovieInfo() {
        // Given

        // When
        movieInfoRepository.deleteById("abc").block();
        var moviesFlux = movieInfoRepository.findAll();

        // Then
        StepVerifier.create(moviesFlux)
                .expectNextCount(2);
    }
}