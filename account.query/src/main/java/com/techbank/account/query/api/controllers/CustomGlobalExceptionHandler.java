package com.techbank.account.query.api.controllers;

import com.techbank.account.common.dto.BaseResponse;
import com.techbank.account.common.dto.ErrorResponse;
import com.techbank.account.query.api.exceptions.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = Logger.getLogger(CustomGlobalExceptionHandler.class.getName());

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<BaseResponse> customHandleNotFound(Exception ex, WebRequest request) {
        logger.log(Level.WARNING, ex.getMessage());

        var httpStatus = HttpStatus.NOT_FOUND;
        return buildResponse(ex, httpStatus);

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
