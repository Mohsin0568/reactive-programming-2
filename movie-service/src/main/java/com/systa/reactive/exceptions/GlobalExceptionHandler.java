package com.systa.reactive.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MoviesInfoClientException.class)
    public ResponseEntity<String> handleMovieInfoClientException(final MoviesInfoClientException exception){
        log.error("Exception caught in handleMovieInfoClientException: {}", exception.getMessage(), exception);
        var errorMessage = exception.getMessage();

        return ResponseEntity.status(HttpStatus.valueOf(exception.getStatusCode())).body(errorMessage);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException exception){
        log.error("Exception caught in handleMovieInfoServerException: {}", exception.getMessage(), exception);
        var errorMessage = exception.getMessage();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

    }
}
