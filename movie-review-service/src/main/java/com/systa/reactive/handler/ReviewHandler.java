package com.systa.reactive.handler;

import com.systa.reactive.domain.Review;
import com.systa.reactive.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ReviewHandler {

    private final ReviewRepository reviewRepository;

    public Mono<ServerResponse> addReview(final ServerRequest serverRequest){
            return serverRequest.bodyToMono(Review.class)
                    .flatMap(reviewRepository :: save)
                    .flatMap(ServerResponse.status(HttpStatus.CREATED) :: bodyValue);
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
