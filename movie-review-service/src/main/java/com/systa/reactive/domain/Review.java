package com.systa.reactive.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Review {

    @Id
    private String reviewId;
    @NotNull(message = "review.movieInfoId should not be null")
    private Long movieInfoId;
    private String comment;
    @Min(value = 0L, message = "review.rating should be a positive value")
    private Double rating;
}
