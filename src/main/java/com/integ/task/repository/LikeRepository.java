package com.integ.task.repository;

import com.integ.task.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByPost_IdAndUser_Id(Long postId, Long userId);

    Optional<Like> findByPost_IdAndUser_Id(Long postId, Long userId);

    long countByPost_Id(Long postId);

    void deleteByPost_Id(Long postId);

}