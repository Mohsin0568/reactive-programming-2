package com.systa.reactive.util;

import com.systa.reactive.domain.MovieInfo;
import com.systa.reactive.domain.Review;
import com.systa.reactive.exceptions.MoviesInfoClientException;
import com.systa.reactive.exceptions.MoviesInfoServerException;
import com.systa.reactive.exceptions.ReviewsClientException;
import com.systa.reactive.exceptions.ReviewsServerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
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
                .onStatus(HttpStatusCode :: is4xxClientError, clientResponse -> {
                    log.error("Received client exception while connecting to movie info service {}",
                            clientResponse.statusCode().value());
                    if(clientResponse.statusCode().value() == HttpStatus.NOT_FOUND.value()){
                        return Mono.error(
                                new MoviesInfoClientException
                                        ("MovieInfo not found with id " + id, clientResponse.statusCode().value()));
                    }
                    else{
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(responseMessage -> {
                                    return Mono.error(
                                            new MoviesInfoClientException(responseMessage, clientResponse.statusCode().value()));
                                });
                    }

                })
                .onStatus(HttpStatusCode :: is5xxServerError, clientResponse -> {
                    log.error("Received server exception while connecting to movie info service {}",
                            clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(new MoviesInfoServerException("Exception in movieService " + responseMessage)));
                })
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
                .onStatus(HttpStatusCode :: is4xxClientError, clientResponse -> {
                    log.error("Received client exception while connecting to review service {}",
                            clientResponse.statusCode().value());
                    if(clientResponse.statusCode().value() == HttpStatus.NOT_FOUND.value()){
                        return Mono.empty();
                    }
                    else{
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(responseMessage -> {
                                    return Mono.error(
                                            new ReviewsClientException(responseMessage, clientResponse.statusCode().value()));
                                });
                    }

                })
                .onStatus(HttpStatusCode :: is5xxServerError, clientResponse -> {
                    log.error("Received server exception while connecting to review service {}",
                            clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.error(new ReviewsServerException("Exception in movieService " + responseMessage)));
                })
                .bodyToFlux(Review.class)
                .log();
    }
}
