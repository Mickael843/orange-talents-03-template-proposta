package com.mikkaeru.exception;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegrity(Exception exception) {
        Problem problem = new Problem(exception.getCause().getMessage(), BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(problem, BAD_REQUEST);
    }

    @ExceptionHandler({FeignException.class})
    protected ResponseEntity<Object> handleFeignException(FeignException exception) {
        Problem problem = new Problem(exception.getMessage(), exception.status(), LocalDateTime.now());
        return new ResponseEntity<>(problem, HttpStatus.valueOf(exception.status()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var fields = new ArrayList<Problem.Field>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String name = ((FieldError) error).getField();
            String msg = messageSource.getMessage(error, Locale.getDefault());

            fields.add(new Problem.Field(name, msg));
        }

        Problem problem = new Problem("Campos Inválidos!", status.value(), LocalDateTime.now(), fields);
        return super.handleExceptionInternal(exception, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var fields = new ArrayList<Problem.Field>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String msg = messageSource.getMessage(error, Locale.getDefault());

            fields.add(new Problem.Field(msg));
        }

        Problem problem = new Problem("Error", status.value(), LocalDateTime.now(), fields);
        return super.handleExceptionInternal(exception, problem, headers, status, request);
    }
}
