package com.integ.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Response structure
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    /**
     * Response code. Generally it will be HTTP status code
     */
    private Integer responseCode;
    /**
     * Response message
     */
    private String responseMessage;
    /**
     * Response object
     */
    private Object responseObject;
}
