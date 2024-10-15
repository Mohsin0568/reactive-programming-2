package com.systa.reactive.handler;

import com.systa.reactive.domain.Review;
import com.systa.reactive.exception.ReviewDataException;
import com.systa.reactive.repository.ReviewRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


@Component
@AllArgsConstructor
@Slf4j
public class ReviewHandler {

    private final ReviewRepository reviewRepository;
    private final Validator validator;

    public Mono<ServerResponse> addReview(final ServerRequest serverRequest){
            return serverRequest.bodyToMono(Review.class)
                    .doOnNext(this :: validate)
                    .flatMap(reviewRepository :: save)
                    .flatMap(ServerResponse.status(HttpStatus.CREATED) :: bodyValue);
    }

    private void validate(final Review review) {

        var violations = validator.validate(review);
        if(!violations.isEmpty()){
            var errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            log.info("There has been data exception {}", errorMessage);
            throw new ReviewDataException(errorMessage);
        }
    }

    public Mono<ServerResponse> getReviews(final ServerRequest serverRequest){
        var movieInfoId = serverRequest.queryParam("movieInfoId");
        if(movieInfoId.isPresent()){
            var reviews = reviewRepository.findReviewByMovieInfoId(Long.valueOf(movieInfoId.get()));
            return ServerResponse.ok().body(reviews, Review.class);
        }
        else{
            var reviews = reviewRepository.findAll();
            return ServerResponse.ok().body(reviews, Review.class);
        }
    }

    public Mono<ServerResponse> updateReview(final ServerRequest serverRequest){
        var reviewId = serverRequest.pathVariable("id");

        var existingReview = reviewRepository.findById(reviewId);

        return existingReview.flatMap(review -> serverRequest
                .bodyToMono(Review.class)
                .map(reqReview -> {
                    review.setComment(reqReview.getComment());
                    review.setRating(reqReview.getRating());
                    return review;
                }))
                .flatMap(reviewRepository :: save)
                .flatMap(ServerResponse.ok() :: bodyValue);
    }

    public Mono<ServerResponse> deleteReview(final ServerRequest serverRequest){
        var reviewId = serverRequest.pathVariable("id");

        return reviewRepository.findById(reviewId)
                .flatMap(review -> reviewRepository.deleteById(reviewId))
                .then(ServerResponse.noContent().build());
    }
}
