package com.integ.task.repository;
import com.integ.task.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_IdAndDeletedFalseOrderByCreatedDesc(Long postId);

    Optional<Comment> findByIdAndDeletedFalse(Long commentId);

    void deleteByPost_Id(Long postId);
}
