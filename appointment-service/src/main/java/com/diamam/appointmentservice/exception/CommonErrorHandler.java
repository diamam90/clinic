package com.diamam.appointmentservice.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> badRequestException(BadRequestException ex) {
        var problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.of(problemDetail).build();
    }
}
