package com.systa.reactive.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReviewNotFoundException extends RuntimeException{

    private final String message;
    private final Throwable ex;
}
