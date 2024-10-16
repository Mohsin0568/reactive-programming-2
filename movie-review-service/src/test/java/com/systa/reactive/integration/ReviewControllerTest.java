package com.systa.reactive.integration;

import com.systa.reactive.domain.Review;
import com.systa.reactive.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ReviewControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReviewRepository reviewRepository;

    private static final String REVIEW_URL = "/v1/review";

    @BeforeEach
    void setUp() {
        var reviews = Arrays.asList(new Review(null, 1L, "Nice Movie", 4.5D),
                new Review(null, 1L, "Nice Movie", 4.5D),
                new Review("abc", 2L, "Nice Movie", 4.5D));

        reviewRepository.saveAll(reviews).blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll().block();
    }

    @Test
    void testAddReview(){
        // Given
        var review = new Review(null, 1L, "Very Nice Movie", 4.5D);

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
    void testGetReviews(){
        // Given

        // When
        webTestClient
                .get()
                .uri(REVIEW_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(3);

        // Then
    }

    @Test
    void testUpdateReview(){
        // Given
        var review = new Review("abc", 2L, "Very Very Nice Movie", 5.0D);
        var reviewId = "abc";

        // When
        webTestClient
                .put()
                .uri(REVIEW_URL+"/{id}", reviewId)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var updatedReview = reviewEntityExchangeResult.getResponseBody();

                    assert updatedReview != null;
                    assertEquals("Very Very Nice Movie", updatedReview.getComment());
                    assertEquals(5.0D, updatedReview.getRating());
                });

        // Then
    }

    @Test
    void testUpdateReview_idNotFound(){
        // Given
        var review = new Review("test", 2L, "Very Very Nice Movie", 5.0D);
        var reviewId = "123";

        // When
        webTestClient
                .put()
                .uri(REVIEW_URL+"/{id}", reviewId)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(String.class)
                .isEqualTo("Review not found with the given review id 123");
//                .consumeWith(reviewEntityExchangeResult -> {
//                    var updatedReview = reviewEntityExchangeResult.getResponseBody();
//
//                    assert updatedReview != null;
//                    assertEquals("Very Very Nice Movie", updatedReview.getComment());
//                    assertEquals(5.0D, updatedReview.getRating());
//                });

        // Then
    }

    @Test
    void testDeleteReview(){
        // Given
        var reviewId = "abc";

        // When
        webTestClient
                .delete()
                .uri(REVIEW_URL+"/{id}", reviewId)
                .exchange()
                .expectStatus()
                .isNoContent();

        // Then
    }
}
