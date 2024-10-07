package com.systa.reactive.service;

import com.systa.reactive.entity.MovieInfo;
import com.systa.reactive.repository.MovieInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MovieInfoService {

    private MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> createMovieInfo(final MovieInfo movieInfo){
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovies() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieById(final String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(final MovieInfo updatedMovieInfo, final String id) {

        return movieInfoRepository.findById(id)
                .flatMap(movieInfo -> {
                    movieInfo.setYear(updatedMovieInfo.getYear());
                    movieInfo.setCast(updatedMovieInfo.getCast());
                    movieInfo.setReleaseDate(updatedMovieInfo.getReleaseDate());
                    movieInfo.setName(updatedMovieInfo.getName());
                    return movieInfoRepository.save(movieInfo);
                });
    }

    public Mono<Void> deleteMovieById(final String id) {
        return movieInfoRepository.deleteById(id);
    }
}
