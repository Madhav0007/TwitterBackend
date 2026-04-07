package com.integ.task.service;

import com.integ.task.dto.LikeDto;
import com.integ.task.entity.Like;

public interface LikeService extends EntityService<Like , LikeDto , Long> {
    LikeDto likePost(Long postId);
    void unlikePost(Long postId);
    long getLikeCount(Long postId);
    boolean isLikedByUser(Long postId);
}