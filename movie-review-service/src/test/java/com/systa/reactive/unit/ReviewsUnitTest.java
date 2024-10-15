package com.systa.reactive.unit;

import com.systa.reactive.domain.Review;
import com.systa.reactive.exception.GlobalErrorHandler;
import com.systa.reactive.handler.ReviewHandler;
import com.systa.reactive.repository.ReviewRepository;
import com.systa.reactive.router.ReviewRouter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalErrorHandler.class})
@AutoConfigureWebTestClient
class ReviewsUnitTest {

    @MockitoBean
    private ReviewRepository reviewRepository;

    @Autowired
    private WebTestClient webTestClient;

    private static final String REVIEW_URL = "/v1/review";

    @Test
    void testAddReview(){
        // Given
        var review = new Review(null, 1L, "Very Nice Movie", 4.5D);
        when(reviewRepository.save(review))
                .thenReturn(Mono.just(new Review("abc", 1L, "Very Nice Movie", 4.5D)));

        // When
        webTestClient
                .post()
                .uri(REVIEW_URL)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var savedReview = reviewEntityExchangeResult.getResponseBody();
                    assertNotNull(savedReview);
                    assertNotNull(savedReview.getReviewId());
                    assertEquals("Very Nice Movie", savedReview.getComment());
                });

        // Then
    }

    @Test
    void testAddReview_validation(){
        // Given
        var review = new Review(null, null, "Very Nice Movie", -4.5D);
        when(reviewRepository.save(review))
                .thenReturn(Mono.just(new Review("abc", 1L, "Very Nice Movie", 4.5D)));

        // When
        webTestClient
                .post()
                .uri(REVIEW_URL)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isBadRequest();

        // Then
    }

}