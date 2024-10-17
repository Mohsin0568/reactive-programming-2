package com.systa.reactive.controller;

import com.systa.reactive.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
@Slf4j
public class MovieController {


    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable("id") final String movieId){

        return null;

    }
}
