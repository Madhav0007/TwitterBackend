package com.integ.task.dto;

import lombok.Data;

@Data
public class PostMediaDto {
    private Long id;
    private Long postId;
    private String mediaUrl;
    private String mediaType;
    private String originalFilename;
}