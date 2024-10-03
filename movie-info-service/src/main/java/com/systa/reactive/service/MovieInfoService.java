package com.systa.reactive.service;

import com.systa.reactive.entity.MovieInfo;
import com.systa.reactive.repository.MovieInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MovieInfoService {

    private MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> createMovieInfo(final MovieInfo movieInfo){
        return movieInfoRepository.save(movieInfo);
    }
}
