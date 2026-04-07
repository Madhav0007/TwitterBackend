package com.integ.task.exceptions;


import com.integ.task.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseDto> handleException(GlobalException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ResponseDto.builder()
                        .responseCode(e.getApplicationStatusCode())
                        .responseObject(e.getMessage())
                        .build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.builder()
                        .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .responseObject(e.getMessage())
                        .build());
    }
}