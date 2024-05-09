package com.techbank.account.cmd.api.controllers;

import com.techbank.account.common.dto.BaseResponse;
import com.techbank.account.common.dto.ErrorResponse;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = Logger.getLogger(CustomGlobalExceptionHandler.class.getName());

    @ExceptionHandler(AggregateNotFoundException.class)
    public ResponseEntity<BaseResponse> customHandleNotFound(Exception ex, WebRequest request) {
        logger.log(Level.WARNING, ex.getMessage());

        var httpStatus = HttpStatus.NOT_FOUND;
        return buildResponse(ex, httpStatus);

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse> customHandleBadRequest(Exception ex, WebRequest request) {
        logger.log(Level.WARNING, ex.getMessage());

        var httpStatus = HttpStatus.BAD_REQUEST;
        return buildResponse(ex, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        var body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    private ResponseEntity<BaseResponse> buildResponse(Exception ex, HttpStatus httpStatus) {
        var response = new ErrorResponse();
        response.setTimestamp(new Date());
        response.setError(httpStatus.getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setStatus(httpStatus.value());

        return new ResponseEntity<>(response, httpStatus);
    }

}
