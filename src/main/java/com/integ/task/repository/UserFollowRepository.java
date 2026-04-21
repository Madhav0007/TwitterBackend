package com.integ.task.repository;

import com.integ.task.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    Optional<UserFollow> findByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    long countByFollower_Id(Long followerId);

    long countByFollowing_Id(Long followingId);

    @Query("select uf.following.id from UserFollow uf where uf.follower.id = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
}