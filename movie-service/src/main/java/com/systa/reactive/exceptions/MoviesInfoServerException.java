package com.systa.reactive.exceptions;

import lombok.Getter;

@Getter
public class MoviesInfoServerException extends RuntimeException{

    private final String message;

    public MoviesInfoServerException(final String message){
        super(message);
        this.message = message;
    }
}
