package com.integ.task.controller;

import com.integ.task.dto.CommentDto;
import com.integ.task.dto.CollectionResponseDto;
import com.integ.task.dto.ResponseDto;
import com.integ.task.entity.Comment;
import com.integ.task.service.CommentService;
import com.integ.task.util.LoggerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController extends BaseController<CommentService , Comment ,CommentDto , Long> {


    /**
     * Constructor
     *
     * @param loggerUtil   Logger util
     * @param service      Service
     */
    protected CommentController(LoggerUtil loggerUtil, CommentService service) {
        super(loggerUtil, service ,"Comment");
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ResponseDto> createComment(@PathVariable Long postId,
                                                     @RequestBody CommentDto requestDto) {
        CommentDto response = service.createComment(postId, requestDto);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Comment added successfully")
                        .responseObject(response)
                        .build()
        );
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CollectionResponseDto> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> comments = service.getCommentsByPostId(postId);

        return ResponseEntity.ok(
                CollectionResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Comments fetched successfully")
                        .responseObject(comments)
                        .totalRecords((long) comments.size())
                        .build()
        );
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDto> updateComment(@PathVariable Long commentId,
                                                     @RequestBody CommentDto requestDto) {
        CommentDto response = service.updateComment(commentId, requestDto);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Comment updated successfully")
                        .responseObject(response)
                        .build()
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage("Comment deleted successfully")
                        .build()
        );
    }
}