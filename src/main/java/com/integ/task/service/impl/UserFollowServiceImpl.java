package com.integ.task.service.impl;

import com.integ.task.dto.UserFollowDto;
import com.integ.task.entity.UserFollow;
import com.integ.task.entity.UserRole;
import com.integ.task.exceptions.GlobalException;
import com.integ.task.repository.UserFollowRepository;
import com.integ.task.repository.UserRepository;
import com.integ.task.service.UserFollowService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserFollowServiceImpl implements UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;

    public UserFollowServiceImpl(UserFollowRepository userFollowRepository,
                                 UserRepository userRepository) {
        this.userFollowRepository = userFollowRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserFollowDto followUser(Long followingUserId) {
        UserRole follower = getCurrentUser();

        if (follower.getId().equals(followingUserId)) {
            throw new GlobalException("You cannot follow yourself", 400, HttpStatus.BAD_REQUEST);
        }

        UserRole following = userRepository.findById(followingUserId)
                .orElseThrow(() -> new GlobalException("User not found", 404, HttpStatus.NOT_FOUND));

        if (userFollowRepository.existsByFollower_IdAndFollowing_Id(follower.getId(), following.getId())) {
            throw new GlobalException("Already following this user", 409, HttpStatus.CONFLICT);
        }

        UserFollow relation = new UserFollow();
        relation.setFollower(follower);
        relation.setFollowing(following);

        return toDto(userFollowRepository.save(relation));
    }

    @Override
    public void unfollowUser(Long followingUserId) {
        UserRole follower = getCurrentUser();

        UserFollow relation = userFollowRepository.findByFollower_IdAndFollowing_Id(
                        follower.getId(), followingUserId)
                .orElseThrow(() -> new GlobalException("Follow relation not found", 404, HttpStatus.NOT_FOUND));

        userFollowRepository.delete(relation);
    }

    @Override
    public boolean isFollowing(Long followingUserId) {
        UserRole follower = getCurrentUser();
        return userFollowRepository.existsByFollower_IdAndFollowing_Id(follower.getId(), followingUserId);
    }

    @Override
    public long getFollowersCount(Long userId) {
        return userFollowRepository.countByFollowing_Id(userId);
    }

    @Override
    public long getFollowingCount(Long userId) {
        return userFollowRepository.countByFollower_Id(userId);
    }

    private UserRole getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new GlobalException("Unauthorized", 401, HttpStatus.UNAUTHORIZED);
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new GlobalException("User not found", 404, HttpStatus.NOT_FOUND));
    }

    private UserFollowDto toDto(UserFollow relation) {
        return new UserFollowDto(
                relation.getId(),
                relation.getFollower().getId(),
                relation.getFollowing().getId()
        );
    }
}