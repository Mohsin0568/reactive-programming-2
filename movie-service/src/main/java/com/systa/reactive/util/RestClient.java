package com.systa.reactive.util;

import com.systa.reactive.domain.MovieInfo;
import com.systa.reactive.domain.Review;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RestClient {

    private final WebClient webClient;

    @Value("${restClient.movieInfoUrl}")
    private String movieInfoUrl;

    @Value("${restClient.movieReviewUrl}")
    private String movieReviewUrl;

    RestClient(final WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<MovieInfo> getMovieById(final String id){

        var url = movieInfoUrl.concat("/{id}");

        return webClient
                .get()
                .uri(url, id)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .log();
    }

    public Flux<Review> getReviewsByMovieId(final String movieId){

        var url = UriComponentsBuilder.fromHttpUrl(movieReviewUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toUriString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class)
                .log();
    }
}
