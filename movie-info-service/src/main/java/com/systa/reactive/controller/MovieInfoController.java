package com.systa.reactive.controller;

import com.systa.reactive.entity.MovieInfo;
import com.systa.reactive.service.MovieInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/moviesInfo")
@AllArgsConstructor
public class MovieInfoController {

    private MovieInfoService movieInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> createMovieInfo(final @RequestBody @Valid MovieInfo movieInfo){
        return movieInfoService.createMovieInfo(movieInfo);
    }

    @GetMapping
    public Flux<MovieInfo> getAllMovies(){
        return movieInfoService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieById(final @PathVariable String id){
        return movieInfoService.getMovieById(id)
                .map(ResponseEntity.ok() :: body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(final @RequestBody MovieInfo movieInfo, final @PathVariable String id){
        return movieInfoService.updateMovieInfo(movieInfo, id)
                .map(ResponseEntity.ok() :: body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieById(final @PathVariable String id){
        return movieInfoService.deleteMovieById(id);
    }
}
