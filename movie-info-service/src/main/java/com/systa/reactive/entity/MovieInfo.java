package com.systa.reactive.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class MovieInfo {

    @Id
    private String movieInfoId;
    @NotBlank(message = "movieInfo.name should not be null")
    private String name;
    @Positive(message = "movieInfo.year should be a positive value")
    private int year;
    private List<@NotBlank(message = "atlease one value should be provided for cast") String> cast;
    private LocalDate releaseDate;
}
