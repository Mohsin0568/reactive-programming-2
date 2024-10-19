package com.systa.reactive.exceptions;

import lombok.Getter;

@Getter
public class MoviesInfoClientException extends RuntimeException{

    private final String message;
    private final Integer statusCode;

    public MoviesInfoClientException(final String message, final Integer statusCode){
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
