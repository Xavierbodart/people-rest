package com.box.people.rest.exception;

import com.box.people.rest.model.base.ErrorMessage;
import com.box.people.rest.model.base.ResultObject;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class DefaultExceptionHandler {
    private String appName = "Error management";

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResultObject<ErrorMessage>> handleException(Exception ex) {
        final List<ErrorMessage> errorMessages = Collections.singletonList(new ErrorMessage(ex.getMessage(), "500"));
        return new ResponseEntity<>(new ResultObject<>(this.appName, false, null, errorMessages),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSuchElementException.class,
            EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ResultObject<ErrorMessage>> handleResourceNotFoundException(Exception ex) {
        final List<ErrorMessage> errorMessages =
                Collections.singletonList(new ErrorMessage("Resource not found: " + ex.getMessage(), "404"));
        return new ResponseEntity<>(new ResultObject<>(this.appName, false, null, errorMessages),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, DataIntegrityViolationException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class})
    public ResponseEntity<ResultObject<ErrorMessage>> handleBadRequests(Exception ex) {
        final List<ErrorMessage> errorMessages =
                Collections.singletonList(new ErrorMessage("Bad request: " + ex.getMessage(), "400"));
        return new ResponseEntity<>(new ResultObject<>(this.appName, false, null, errorMessages),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultObject<ErrorMessage>> notValid(MethodArgumentNotValidException ex) {
        List<ErrorMessage> errorMessages =
                ex.getAllErrors().stream().map(error -> new ErrorMessage(error.getDefaultMessage(), "400")).collect(Collectors.toList());
        return new ResponseEntity<>(new ResultObject<>(this.appName, false, null, errorMessages),
                HttpStatus.BAD_REQUEST);
    }

}