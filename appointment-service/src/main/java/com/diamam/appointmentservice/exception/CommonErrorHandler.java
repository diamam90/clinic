package com.diamam.appointmentservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> badRequestException(BadRequestException ex) {
        var problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setDetail(ex.getMessage());
        log.info("Bad request for {}", problemDetail);
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handlerMethodValidationException(HandlerMethodValidationException ex) {

        ProblemDetail problemDetail;
        if (ex.hasErrors()) {
            var message = ex.getAllErrors().stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        } else {
            problemDetail = ProblemDetail.forStatus(400);
        }
        log.info("Validation failed for {}", problemDetail);
        return ResponseEntity.of(problemDetail).build();
    }
}
