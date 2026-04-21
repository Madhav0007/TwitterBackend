package com.integ.task.service;

import com.integ.task.dto.UserFollowDto;

public interface UserFollowService {

    UserFollowDto followUser(Long followingUserId);

    void unfollowUser(Long followingUserId);

    boolean isFollowing(Long followingUserId);

    long getFollowersCount(Long userId);

    long getFollowingCount(Long userId);
}