package com.integ.task.repository;

import com.integ.task.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    List<PostMedia> findByPost_IdOrderByIdAsc(Long postId);
    void deleteByPost_Id(Long postId);
}