package com.integ.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowDto {
    private Long id;
    private Long followerId;
    private Long followingId;
}