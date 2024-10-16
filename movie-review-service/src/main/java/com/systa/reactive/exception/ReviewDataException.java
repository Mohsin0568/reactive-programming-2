package com.systa.reactive.exception;

public class ReviewDataException extends RuntimeException{

    private final String message;

    public ReviewDataException(String message) {
        super(message);
        this.message = message;
    }
}
