package com.integ.task.service;

import com.integ.task.dto.CommentDto;
import com.integ.task.entity.Comment;

import java.util.List;

public interface CommentService extends EntityService<Comment, CommentDto, Long> {

    CommentDto createComment(Long postId, CommentDto requestDto);

    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto updateComment(Long commentId, CommentDto requestDto);

    void deleteComment(Long commentId);
}