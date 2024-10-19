package com.systa.reactive.exceptions;

import lombok.Getter;

@Getter
public class ReviewsClientException extends RuntimeException{

    private final String message;
    private final Integer statusCode;

    public ReviewsClientException(final String message, final Integer statusCode){
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
