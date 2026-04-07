package com.integ.task.service.impl;

import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.dto.PostDto;
import com.integ.task.entity.Post;
import com.integ.task.repository.PostRepository;
import com.integ.task.service.LikeService;
import com.integ.task.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private  final PostRepository postRepository;
    private final LikeService likeService;
    public PostServiceImpl(PostRepository postRepository, LikeService likeService) {
        this.postRepository = postRepository;
        this.likeService = likeService;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    @Override
    public PostDto save(PostDto dto) {
        Post post = dtotomodel(dto);
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
    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }


    private PostDto modeltodto(Post post) {
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setName(post.getName());
        postDto.setDescription(post.getDescription());
        postDto.setImageUrl(post.getImageUrl());
        postDto.setLikeCount(likeService.getLikeCount(post.getId()));
        postDto.setLikedByUser(likeService.isLikedByUser(post.getId()));

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
