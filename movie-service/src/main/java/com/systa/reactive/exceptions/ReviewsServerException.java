package com.systa.reactive.exceptions;

import lombok.Getter;

@Getter
public class ReviewsServerException  extends RuntimeException{

    private final String message;

    public ReviewsServerException(final String message){
        super(message);
        this.message = message;
    }
}
