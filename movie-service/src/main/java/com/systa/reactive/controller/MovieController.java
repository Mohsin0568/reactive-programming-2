package com.systa.reactive.controller;

import com.systa.reactive.domain.Movie;
import com.systa.reactive.util.RestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
@Slf4j
@AllArgsConstructor
public class MovieController {

    private final RestClient restClient;

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable("id") final String movieId){

        return restClient.getMovieById(movieId)
                .flatMap(movie -> {
                    var reviewListMono = restClient.getReviewsByMovieId(movieId)
                            .collectList();

                    return reviewListMono.map(reviews -> new Movie(movie, reviews));
                });
    }
}
