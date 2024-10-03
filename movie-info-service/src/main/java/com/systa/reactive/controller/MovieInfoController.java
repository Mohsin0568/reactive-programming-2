package com.systa.reactive.controller;

import com.systa.reactive.entity.MovieInfo;
import com.systa.reactive.service.MovieInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class MovieInfoController {

    private MovieInfoService movieInfoService;

    @PostMapping("/moviesInfo")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> createMovieInfo(final @RequestBody MovieInfo movieInfo){
        return movieInfoService.createMovieInfo(movieInfo);
    }
}
