package com.integ.task.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@Setter
@Getter
public class GlobalException extends RuntimeException {

    private final int applicationStatusCode;
    private final HttpStatus httpStatus;

    public GlobalException(String message, Throwable throwable){
        super(message, throwable);
        this.applicationStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GlobalException(String message){
        this(new Throwable(message));
    }

    public GlobalException(Throwable throwable){
        this(ObjectUtils.isEmpty(throwable) ? "Unknown Error" : throwable.getMessage(), throwable);
    }

    public GlobalException(String message, int applicationStatusCode, HttpStatus httpStatus) {
        super(message);
        this.applicationStatusCode = applicationStatusCode;
        this.httpStatus = httpStatus;
    }
}
