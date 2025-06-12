package com.fbr.jbank.exception.global;

import com.fbr.jbank.controller.dto.InvalidParamErrorDto;
import com.fbr.jbank.exception.JbankException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JbankException.class)
    public ProblemDetail handleJbankException(JbankException e) {
        return e.problemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var invalidParams = e.getFieldErrors()
                .stream()
                .map(fe -> new InvalidParamErrorDto(fe.getField(), fe.getDefaultMessage()))
                .toList();

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Invalid request parameters");
        pb.setDetail("There is invalid fields on the request");
        pb.setProperty("invalid-params", invalidParams);

        return pb;
    }
}
