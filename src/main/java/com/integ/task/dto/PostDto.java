package com.integ.task.dto;

import lombok.Data;

@Data

public class PostDto {
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private String creatorName;
    private java.time.LocalDateTime creationDate;
    private long likeCount;
    private boolean likedByUser;
}
