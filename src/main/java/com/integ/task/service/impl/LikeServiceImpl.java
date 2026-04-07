package com.integ.task.service.impl;

import com.integ.task.dto.LikeDto;
import com.integ.task.dto.PostDto;
import com.integ.task.entity.Like;
import com.integ.task.entity.Post;
import com.integ.task.entity.UserRole;
import com.integ.task.exceptions.GlobalException;
import com.integ.task.repository.LikeRepository;
import com.integ.task.repository.PostRepository;
import com.integ.task.repository.UserRepository;
import com.integ.task.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeServiceImpl(LikeRepository likeRepository,
                           PostRepository postRepository,
                           UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LikeDto likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GlobalException("Post not found", 404, HttpStatus.NOT_FOUND));

        UserRole user = getCurrentUser();

        if (likeRepository.existsByPost_IdAndUser_Id(postId, user.getId())) {
            throw new GlobalException("You already liked this post", 409, HttpStatus.CONFLICT);
        }

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        return toDto(likeRepository.save(like));
    }

    @Override
    public void unlikePost(Long postId) {
        UserRole user = getCurrentUser();

        Like like = likeRepository.findByPost_IdAndUser_Id(postId, user.getId())
                .orElseThrow(() -> new GlobalException("Like not found", 404, HttpStatus.NOT_FOUND));

        likeRepository.delete(like);
    }

    @Override
    public long getLikeCount(Long postId) {
        return likeRepository.countByPost_Id(postId);
    }

    @Override
    public LikeDto save(LikeDto dto) {
        throw new GlobalException("Use likePost API instead", 400, HttpStatus.BAD_REQUEST);
    }

    @Override
    public LikeDto update(LikeDto dto) {
        throw new GlobalException("Like update not supported", 400, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }

    @Override
    public LikeDto findById(Long id) {
        Like like = likeRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Like not found", 404, HttpStatus.NOT_FOUND));
        return toDto(like);
    }

    @Override
    public List<LikeDto> findAll() {
        return likeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public org.springframework.data.domain.Page<LikeDto> paginate(com.integ.task.dto.PaginationRequestDTO paginationRequestDTO) {
        throw new GlobalException("Pagination not implemented for likes yet", 400, HttpStatus.BAD_REQUEST);
    }

    @Override
    public Like dtoToModel(LikeDto dto) {
        throw new GlobalException("Not used", 400, HttpStatus.BAD_REQUEST);
    }

    @Override
    public Like dtoToModel(LikeDto dto, Like model) {
        throw new GlobalException("Not used", 400, HttpStatus.BAD_REQUEST);
    }

    @Override
    public LikeDto modelToDto(Like model) {
        return toDto(model);
    }

    private LikeDto toDto(Like like) {
        return new LikeDto(
                like.getId(),
                like.getPost().getId(),
                like.getUser().getId()
        );
    }

    private UserRole getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalException("User not found", 404, HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean isLikedByUser(Long postId) {
        UserRole user = getCurrentUser();
        return likeRepository.existsByPost_IdAndUser_Id(postId, user.getId());
    }
}