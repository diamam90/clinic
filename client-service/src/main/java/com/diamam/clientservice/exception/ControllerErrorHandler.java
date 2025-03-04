package com.diamam.clientservice.exception;

import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFoundHandler(ObjectNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(404);
        problem.setDetail(ex.getLocalizedMessage());
        return ResponseEntity.of(problem).build();
    }

    @ExceptionHandler({BindValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ProblemDetail> validateException(BindingResult result) {
        String errorMessage = result.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ProblemDetail problem = ProblemDetail.forStatus(400);
        problem.setDetail(errorMessage);
        return ResponseEntity.of(problem).build();
    }
}
