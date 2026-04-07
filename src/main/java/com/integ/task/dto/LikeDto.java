package com.integ.task.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
    private Long id;
    private Long postId;
    private Long userId;
}