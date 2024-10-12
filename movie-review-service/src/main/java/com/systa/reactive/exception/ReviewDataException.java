package com.systa.reactive.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReviewDataException extends RuntimeException{

    private final String message;
}
