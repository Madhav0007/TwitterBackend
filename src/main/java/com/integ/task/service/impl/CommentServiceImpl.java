package com.integ.task.service.impl;

import com.integ.task.dto.CommentDto;
import com.integ.task.entity.Comment;
import com.integ.task.entity.Post;
import com.integ.task.entity.UserRole;
import com.integ.task.exceptions.GlobalException;
import com.integ.task.repository.CommentRepository;
import com.integ.task.repository.PostRepository;
import com.integ.task.repository.UserRepository;
import com.integ.task.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GlobalException("Post not found", 404, HttpStatus.NOT_FOUND));

        UserRole currentUser = getCurrentUser();

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(currentUser);
        comment.setContent(requestDto.getContent());
        comment.setDeleted(false);

        Comment saved = commentRepository.save(comment);
        return toResponseDto(saved);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GlobalException("Post not found", 404, HttpStatus.NOT_FOUND));

        return commentRepository.findByPost_IdAndDeletedFalseOrderByCreatedDesc(post.getId())
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public CommentDto updateComment(Long commentId, CommentDto requestDto) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new GlobalException("Comment not found", 404, HttpStatus.NOT_FOUND));

        UserRole currentUser = getCurrentUser();
        checkOwnerOrAdmin(comment, currentUser);

        comment.setContent(requestDto.getContent());

        Comment saved = commentRepository.save(comment);
        return toResponseDto(saved);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new GlobalException("Comment not found", 404, HttpStatus.NOT_FOUND));

        UserRole currentUser = getCurrentUser();
        checkOwnerOrAdmin(comment, currentUser);

        comment.setDeleted(true);
        commentRepository.save(comment);
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

    private void checkOwnerOrAdmin(Comment comment, UserRole currentUser) {
        boolean isOwner = comment.getUser().getUsername().equals(currentUser.getUsername());
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());

        if (!isOwner && !isAdmin) {
            throw new GlobalException("You are not allowed to modify this comment", 403, HttpStatus.FORBIDDEN);
        }
    }

    private CommentDto toResponseDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getUsername(),
                comment.getContent()
        );
    }
}