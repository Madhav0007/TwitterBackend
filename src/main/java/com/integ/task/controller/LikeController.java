package com.integ.task.controller;

import com.integ.task.dto.LikeDto;
import com.integ.task.dto.ResponseDto;
import com.integ.task.entity.Like;
import com.integ.task.service.LikeService;
import com.integ.task.util.LoggerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController extends BaseController<LikeService, Like, LikeDto, Long> {

    protected LikeController(LoggerUtil loggerUtil, LikeService service) {
        super(loggerUtil, service, "Like");
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ResponseDto> likePost(@PathVariable Long postId) {
        LikeDto liked = service.likePost(postId);
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Post liked successfully")
                        .responseObject(liked)
                        .build()
        );
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseDto> unlikePost(@PathVariable Long postId) {
        service.unlikePost(postId);
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Post unliked successfully")
                        .build()
        );
    }

    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<ResponseDto> countLikes(@PathVariable Long postId) {
        long count = service.getLikeCount(postId);
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Like count fetched successfully")
                        .responseObject(count)
                        .build()
        );
    }
}