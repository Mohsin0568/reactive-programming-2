package com.systa.reactive.exceptions;

import lombok.Getter;

@Getter
public class ReviewsClientException extends RuntimeException{

    private final String message;

    ReviewsClientException(final String message){
        super(message);
        this.message = message;
    }
}
