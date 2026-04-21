package com.integ.task.service;

import com.integ.task.dto.PostMediaDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostMediaService {
    List<PostMediaDto> uploadMedia(Long postId, MultipartFile[] files);
    List<PostMediaDto> getMediaByPostId(Long postId);
    void deleteMedia(Long postId, Long mediaId);
}