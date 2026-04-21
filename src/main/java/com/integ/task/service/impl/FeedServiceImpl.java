package com.integ.task.service.impl;

import com.integ.task.dto.PaginationRequestDTO;
import com.integ.task.dto.PostDto;
import com.integ.task.dto.PostMediaDto;
import com.integ.task.entity.Post;
import com.integ.task.entity.UserRole;
import com.integ.task.repository.PostMediaRepository;
import com.integ.task.repository.PostRepository;
import com.integ.task.repository.UserFollowRepository;
import com.integ.task.repository.UserRepository;
import com.integ.task.service.FeedService;
import com.integ.task.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FeedServiceImpl implements FeedService {

    private final PostMediaRepository postMediaRepository;
    private final PostRepository postRepository;
    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;

    public FeedServiceImpl(PostMediaRepository postMediaRepository, PostRepository postRepository,
                           UserFollowRepository userFollowRepository,
                           UserRepository userRepository,
                           LikeService likeService) {
        this.postMediaRepository = postMediaRepository;
        this.postRepository = postRepository;
        this.userFollowRepository = userFollowRepository;
        this.userRepository = userRepository;
        this.likeService = likeService;
    }

    @Override
    public Page<PostDto> getFeed(PaginationRequestDTO dto) {
        UserRole currentUser = getCurrentUser();

        List<Long> followingIds = userFollowRepository.findFollowingIdsByFollowerId(currentUser.getId());

        Set<Long> authorIds = new HashSet<>(followingIds);
        authorIds.add(currentUser.getId()); // include own posts

        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                Sort.by(Sort.Direction.DESC, "creationDate")
        );

        Page<Post> posts = postRepository.findByCreatorUser_IdIn(authorIds, pageable);
        return posts.map(this::toDto);
    }

    private PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setName(post.getName());
        dto.setDescription(post.getDescription());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatorName(post.getCreatorName());
        dto.setCreatorUserId(post.getCreatorUser().getId());
        dto.setCreationDate(post.getCreationDate());

        dto.setLikeCount(likeService.getLikeCount(post.getId()));
        dto.setLikedByUser(likeService.isLikedByUser(post.getId()));
        dto.setMedia(
                postMediaRepository.findByPost_IdOrderByIdAsc(post.getId())
                        .stream()
                        .map(media -> {
                            PostMediaDto m = new PostMediaDto();
                            m.setId(media.getId());
                            m.setPostId(post.getId());
                            m.setMediaUrl(media.getMediaUrl());
                            m.setMediaType(media.getMediaType());
                            m.setOriginalFilename(media.getOriginalFilename());
                            return m;
                        })
                        .toList()
        );

        return dto;
    }

    private UserRole getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}