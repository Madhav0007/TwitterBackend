package com.integ.task.service.impl;

import com.integ.task.dto.PostMediaDto;
import com.integ.task.entity.Post;
import com.integ.task.entity.PostMedia;
import com.integ.task.exceptions.GlobalException;
import com.integ.task.repository.PostMediaRepository;
import com.integ.task.repository.PostRepository;
import com.integ.task.service.FileStorageService;
import com.integ.task.service.PostMediaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostMediaServiceImpl implements PostMediaService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final FileStorageService fileStorageService;

    public PostMediaServiceImpl(PostRepository postRepository,
                                PostMediaRepository postMediaRepository,
                                FileStorageService fileStorageService) {
        this.postRepository = postRepository;
        this.postMediaRepository = postMediaRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<PostMediaDto> uploadMedia(Long postId, MultipartFile[] files) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GlobalException("Post not found", 404, HttpStatus.NOT_FOUND));

        if (files == null || files.length == 0) {
            throw new GlobalException("No files provided", 400, HttpStatus.BAD_REQUEST);
        }

        return java.util.Arrays.stream(files)
                .map(file -> {
                    String mediaType = file.getContentType();
                    if (mediaType == null || !(mediaType.startsWith("image/") || mediaType.startsWith("video/"))) {
                        throw new GlobalException("Only image/video files are allowed", 400, HttpStatus.BAD_REQUEST);
                    }

                    String path = fileStorageService.store(file);

                    PostMedia media = new PostMedia();
                    media.setPost(post);
                    media.setMediaUrl(path);
                    media.setMediaType(mediaType);
                    media.setOriginalFilename(file.getOriginalFilename());

                    return toDto(postMediaRepository.save(media));
                })
                .toList();
    }

    @Override
    public List<PostMediaDto> getMediaByPostId(Long postId) {
        return postMediaRepository.findByPost_IdOrderByIdAsc(postId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void deleteMedia(Long postId, Long mediaId) {
        PostMedia media = postMediaRepository.findById(mediaId)
                .orElseThrow(() -> new GlobalException("Media not found", 404, HttpStatus.NOT_FOUND));

        if (!media.getPost().getId().equals(postId)) {
            throw new GlobalException("Media does not belong to this post", 400, HttpStatus.BAD_REQUEST);
        }

        fileStorageService.delete(media.getMediaUrl());
        postMediaRepository.delete(media);
    }

    private PostMediaDto toDto(PostMedia media) {
        PostMediaDto dto = new PostMediaDto();
        dto.setId(media.getId());
        dto.setPostId(media.getPost().getId());
        dto.setMediaUrl(media.getMediaUrl());
        dto.setMediaType(media.getMediaType());
        dto.setOriginalFilename(media.getOriginalFilename());
        return dto;
    }
}