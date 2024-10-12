package com.systa.reactive.repository;

import com.systa.reactive.domain.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {

        var reviews = Arrays.asList(new Review(null, 1L, "Nice Movie", 4.5D),
                new Review(null, 2L, "Nice Movie", 4.5D),
                new Review("abc", 3L, "Nice Movie", 4.5D));

        reviewRepository.saveAll(reviews).blockLast();
        
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll().block();
    }

    @Test
    void testFindAll(){
        // Given

        // When
        var allReviews = reviewRepository.findAll();

        // Then
        StepVerifier.create(allReviews)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void testFindById(){
        // Given
        var id = "abc";

        // When
        var reviewMono = reviewRepository.findById(id);

        // Then
        StepVerifier.create(reviewMono)
                //.expectNextCount(1)
                .assertNext(review -> {
                    assertNotNull(review);
                    assertEquals(3L, review.getMovieInfoId());
                })
                .verifyComplete();
    }
}