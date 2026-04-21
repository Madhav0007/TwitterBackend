package com.integ.task.controller;

import com.integ.task.dto.PostMediaDto;
import com.integ.task.dto.ResponseDto;
import com.integ.task.service.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostMediaController {

    private final PostMediaService postMediaService;

    @PostMapping(value = "/{postId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> uploadMedia(@PathVariable Long postId,
                                                   @RequestParam("files") MultipartFile[] files) {
        List<PostMediaDto> uploaded = postMediaService.uploadMedia(postId, files);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Media uploaded successfully")
                        .responseObject(uploaded)
                        .build()
        );
    }

    @GetMapping("/{postId}/media")
    public ResponseEntity<ResponseDto> getMedia(@PathVariable Long postId) {
        List<PostMediaDto> media = postMediaService.getMediaByPostId(postId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Media fetched successfully")
                        .responseObject(media)
                        .build()
        );
    }

    @DeleteMapping("/{postId}/media/{mediaId}")
    public ResponseEntity<ResponseDto> deleteMedia(@PathVariable Long postId,
                                                   @PathVariable Long mediaId) {
        postMediaService.deleteMedia(postId, mediaId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Media deleted successfully")
                        .build()
        );
    }
}