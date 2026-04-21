package com.integ.task.dto;

import lombok.Data;

import java.util.List;

@Data

public class PostDto {
    private Long id;

    private String name;
    private String description;
    private String imageUrl;

    private String creatorName;
    private Long creatorUserId;

    private java.time.LocalDateTime creationDate;
    private long likeCount;
    private boolean likedByUser;

    private List<PostMediaDto> media;
}
