package com.integ.task.service.impl;

import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.dto.PostDto;
import com.integ.task.dto.PostMediaDto;
import com.integ.task.entity.Post;
import com.integ.task.entity.PostMedia;
import com.integ.task.entity.UserRole;
import com.integ.task.repository.*;
import com.integ.task.service.FileStorageService;
import com.integ.task.service.LikeService;
import com.integ.task.service.PostService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final LikeService likeService;
    private final FileStorageService fileStorageService;

    private final PostMediaRepository postMediaRepository;
    private final LikeRepository likeRepository;
    private  final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostMediaRepository postMediaRepository, PostRepository postRepository, LikeService likeService, LikeRepository likeRepository, UserRepository userRepository, CommentRepository commentRepository, FileStorageService fileStorageService) {
        this.postMediaRepository = postMediaRepository;
        this.postRepository = postRepository;
        this.likeService = likeService;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.fileStorageService = fileStorageService;
    }

    private UserRole getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    @Override
    public PostDto save(PostDto dto) {
        UserRole currentUser = getCurrentUser();

        Post post = dtotomodel(dto);
        post.setCreatorUser(currentUser);
        post.setCreatorName(currentUser.getUsername());
        post.setCreationDate(LocalDateTime.now());

        return modeltodto(postRepository.save(post));
    }

    @Override
    public PostDto findById(Long id) {
        return modeltodto(getPostById(id));
    }

    @Override
    public List<PostDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(this::modeltodto)
                .toList();
    }

    @Override
    public PostDto update(PostDto dto) {
        Post existing = postRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());
        existing.setCreatorName(dto.getCreatorName());

        return modeltodto(postRepository.save(existing));
    }

    @Transactional
    @Override
    public void delete(Long postId) {

        // 1. fetch media
        List<PostMedia> mediaList = postMediaRepository.findByPost_IdOrderByIdAsc(postId);

        // 2. delete files from disk
        for (PostMedia media : mediaList) {
            fileStorageService.delete(media.getMediaUrl());
        }

        // 3. delete child records FIRST
        postMediaRepository.deleteByPost_Id(postId);
        likeRepository.deleteByPost_Id(postId);
        commentRepository.deleteByPost_Id(postId);

        // 4. delete post
        postRepository.deleteById(postId);
    }

    private PostDto modeltodto(Post post) {
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setName(post.getName());
        postDto.setDescription(post.getDescription());
        postDto.setImageUrl(post.getImageUrl());
        postDto.setCreatorName(post.getCreatorName());
        postDto.setCreatorUserId(post.getCreatorUser() != null ? post.getCreatorUser().getId() : null);
        postDto.setCreationDate(post.getCreationDate());
        postDto.setLikeCount(likeService.getLikeCount(post.getId()));
        postDto.setLikedByUser(likeService.isLikedByUser(post.getId()));
        postDto.setMedia(
                postMediaRepository.findByPost_IdOrderByIdAsc(post.getId())
                        .stream()
                        .map(media -> {
                            PostMediaDto dto = new PostMediaDto();
                            dto.setId(media.getId());
                            dto.setPostId(post.getId());
                            dto.setMediaUrl(media.getMediaUrl());
                            dto.setMediaType(media.getMediaType());
                            dto.setOriginalFilename(media.getOriginalFilename());
                            return dto;
                        })
                        .toList()
        );

        return postDto;
    }

    private Post dtotomodel(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setName(postDto.getName());
        post.setDescription(postDto.getDescription());
        post.setImageUrl(postDto.getImageUrl());
        post.setCreatorName(postDto.getCreatorName());
        return post;
    }

    private org.springframework.data.domain.Sort toSpringSort(com.integ.task.util.Sort sort) {
        if (sort == null || sort.getColumn() == null || sort.getOrder() == null) {
            return org.springframework.data.domain.Sort.by("creationDate").descending();
        }

        String column = sort.getColumn().trim();
        String order = sort.getOrder().trim().toLowerCase();

        return switch (column) {

            case "likes" -> throw new RuntimeException("Sorting by likes not supported yet");

            case "name" -> "asc".equals(order)
                    ? org.springframework.data.domain.Sort.by("name").ascending()
                    : org.springframework.data.domain.Sort.by("name").descending();

            case "creationDate" -> "asc".equals(order)
                    ? org.springframework.data.domain.Sort.by("creationDate").ascending()
                    : org.springframework.data.domain.Sort.by("creationDate").descending();

            default -> org.springframework.data.domain.Sort.by("creationDate").descending();
        };
    }

    @Override
    public Page<PostDto> paginate(PaginationRequestDTO dto) {
        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                toSpringSort(dto.getSort())
        );

        return postRepository.findAll(pageable)
                .map(this::modeltodto);
    }
}
